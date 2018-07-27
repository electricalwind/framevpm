package framevpm.learning.approaches.ifc;

import framevpm.analyze.approaches.filebyfile.FileFunctionCalls;
import framevpm.analyze.model.Analysis;
import framevpm.learning.approaches.Approach;
import framevpm.learning.model.Experiment;
import framevpm.learning.model.FileMetaInf;
import framevpm.learning.model.classmodel.ClassModel;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import java.util.*;

@SuppressWarnings("Duplicates")
public class FunctionCallsApproach extends Approach {

    public FunctionCallsApproach(List<Experiment> experiments, ClassModel model) {
        super(experiments, model);
    }

    @Override
    public void prepareInstances() {
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
            values[i] = (int) stringAnalysisMap.get(FileFunctionCalls.NAME).getFeatureMap().getOrDefault(name,0);
        }
        values[featureVector.size() - 1] = model.getClassList().indexOf(type);
        return new SparseInstance(1, values);
    }

    private ArrayList<Attribute> generateFeatureVector(LinkedHashMap<FileMetaInf, Map<String, Analysis>> experiment) {
        String interestingClass = model.getClassList().get(0);
        Map<String, Integer> countFC = new HashMap<>();
        int[] i = {0};
        experiment.forEach((file, analysis) -> {
            if (model.correspondingToTypeFile(file.getType()).equals(interestingClass)) {
                i[0]++;
                analysis.get(FileFunctionCalls.NAME).getFeatureMap().keySet().forEach(fc -> countFC.put(fc, countFC.getOrDefault(fc, 0) + 1));
            }
        });

        int threshold = (i[0] * 5 / 100) + 1;
        ArrayList<Attribute> attributes = new ArrayList<>();
        countFC.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getValue() > threshold).forEach(stringIntegerEntry -> attributes.add(new Attribute(stringIntegerEntry.getKey())));

        attributes.add(new Attribute("theClass", model.getClassList()));
        return attributes;
    }

    @Override
    public String getApproachName() {
        return "FunctionCalls";
    }
}
