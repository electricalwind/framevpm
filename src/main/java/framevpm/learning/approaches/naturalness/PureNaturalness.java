package framevpm.learning.approaches.naturalness;

import framevpm.analyze.approaches.filebyfile.FileIncludes;
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

public class PureNaturalness extends Approach {

    public PureNaturalness(List<Experiment> experiments, ClassModel model) {
        super(experiments, model);
    }

    @Override
    public void prepareInstances() {
        for (Experiment experiment : experiments) {
            ArrayList<Attribute> featureVector = generateFeatureVector(experiment);
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
            values[i] = (double) stringAnalysisMap.get(name).getFeatureMap().get("cross-Entropy");
        }
        values[featureVector.size() - 1] = model.getClassList().indexOf(type);
        return new SparseInstance(1, values);
    }

    private ArrayList<Attribute> generateFeatureVector(Experiment experiment) {
        Set<String> availableMesure = new HashSet<>();
        boolean[] first = {true};
        experiment.getTraining().forEach((file, analysis) -> {
            analysis.keySet().stream().filter(approach -> approach.contains("Naturalness")).forEach(natural -> {
                if (first[0]) {
                    availableMesure.add(natural);
                } else {
                    if (!availableMesure.contains(natural)) {
                        availableMesure.remove(natural);
                    }
                }
            });
        });

        experiment.getTesting().forEach((file, analysis) -> {
            analysis.keySet().stream().filter(approach -> approach.contains("Naturalness")).forEach(natural -> {
                if (!availableMesure.contains(natural)) {
                    availableMesure.remove(natural);
                }
            });
        });
        ArrayList<Attribute> attributes = new ArrayList<>();
        availableMesure.forEach(mesure-> attributes.add(new Attribute(mesure)));
        attributes.add(new Attribute("theClass", model.getClassList()));
        return attributes;
    }

    @Override
    public String getApproachName() {
        return "PureNaturalness";
    }
}
