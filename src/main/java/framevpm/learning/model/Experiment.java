package framevpm.learning.model;

import framevpm.analyze.model.Analysis;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Experiment implements Serializable {

    private static final long serialVersionUID = 20180721L;

    private final String name;
    private final LinkedHashMap<FileMetaInf,Map<String,Analysis>> training;

    private final LinkedHashMap<FileMetaInf,Map<String,Analysis>> testing;

    public Experiment(String name, LinkedHashMap<FileMetaInf, Map<String, Analysis>> training, LinkedHashMap<FileMetaInf, Map<String, Analysis>> testing) {
        this.name = name;
        this.training = training;
        this.testing = testing;
    }

    public LinkedHashMap<FileMetaInf,Map<String,Analysis>> getTraining() {
        return training;
    }

    public LinkedHashMap<FileMetaInf,Map<String,Analysis>> getTesting() {
        return testing;
    }

    public String getName() {
        return name;
    }
}
