package framevpm.learning.approaches.ifc;

import framevpm.analyze.approaches.filebyfile.FileIncludes;
import framevpm.analyze.model.Analysis;
import framevpm.learning.approaches.Approach;
import framevpm.learning.model.Experiment;
import framevpm.learning.model.FileMetaInf;
import framevpm.learning.model.classmodel.ClassModel;
import weka.core.*;

import java.util.*;

public class IncludesApproach extends Approach {

    public IncludesApproach(List<Experiment> experiments, ClassModel model) {
        super(experiments, model);
    }

    @Override
    protected void prepareInstances() {
        for (Experiment experiment : experiments) {
            ArrayList<Attribute> featureVector = generateFeatureVector(experiment.getTraining());
            if (featureVector.size() > 1) {
                Instances training = generateInstances("training", experiment.getTraining(), featureVector);
                Instances testing = generateInstances("testing", experiment.getTesting(), featureVector);
                preparedInstances.put(experiment.getName(), new Instances[]{training, testing});
            }
        }
    }

    private Instances generateInstances(String type, LinkedHashMap<FileMetaInf, Map<String, Analysis>> set, ArrayList<Attribute> featureVector) {
        Instances instances = new Instances(type, featureVector, set.size());
        instances.setClassIndex(featureVector.size() - 1);
        set.forEach((fileMetaInf, stringAnalysisMap) -> {
            Instance instance = generateInstance(featureVector, fileMetaInf, stringAnalysisMap);
            if (instance != null) {
                instance.setDataset(instances);
                instances.add(instance);
            }
        });
        return instances;
    }

    private Instance generateInstance(ArrayList<Attribute> featureVector, FileMetaInf fileMetaInf, Map<String, Analysis> stringAnalysisMap) {
        double[] values = new double[featureVector.size()];
        String type = model.correspondingToTypeFile(fileMetaInf.getType());
        if (type == null) return null;
        for (int i = 0; i < featureVector.size() - 1; i++) {
            String name = featureVector.get(i).name();
            if (stringAnalysisMap.get(FileIncludes.NAME).getFeatureMap().containsKey(name)) values[i] = 1;
            else values[i] = 0;
        }
        values[featureVector.size() - 1] = model.getClassList().indexOf(type);
        return new DenseInstance(1, values);
    }

    private ArrayList<Attribute> generateFeatureVector(LinkedHashMap<FileMetaInf, Map<String, Analysis>> experiment) {
        String interestingClass = model.getClassList().get(0);
        Map<String, Integer> countIncludes = new HashMap<>();
        int[] i = {0};
        experiment.forEach((file, analysis) -> {
            if (model.correspondingToTypeFile(file.getType()).equals(interestingClass)) {
                i[0]++;
                analysis.get(FileIncludes.NAME).getFeatureMap().keySet().forEach(include -> countIncludes.put(include, countIncludes.getOrDefault(include, 0)+1));
            }
        });

        int threshold = (i[0] * 3 / 100) + 1;
        ArrayList<Attribute> attributes = new ArrayList<>();
        countIncludes.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getValue() > threshold).forEach(stringIntegerEntry -> attributes.add(new Attribute(stringIntegerEntry.getKey())));

        attributes.add(new Attribute("theClass", model.getClassList()));
        return attributes;
    }

    @Override
    public String getApproachName() {
        return "Includes";
    }
}
