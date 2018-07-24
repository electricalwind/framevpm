package framevpm.learning.model;

import framevpm.analyze.model.FileAnalysis;

import java.util.List;

public class ExperimentalData {
    private final List<FileAnalysis> training;

    private final List<FileAnalysis> testing;

    public ExperimentalData(List<FileAnalysis> training, List<FileAnalysis> testing) {
        this.training = training;
        this.testing = testing;
    }

    public List<FileAnalysis> getTraining() {
        return training;
    }

    public List<FileAnalysis> getTesting() {
        return testing;
    }
}
