package framevpm.learning.model;

import java.io.Serializable;

public class ExperimentResult implements Serializable {

    private static final long serialVersionUID = 20180727L;

    private final double[] distribresult;
    private final double classification;
    private final FileMetaInf fileMetaInf;
    private final double expectedClassif;

    public ExperimentResult(double[] distribresult, double classification, FileMetaInf fileMetaInf, double expectedClassif) {
        this.distribresult = distribresult;
        this.classification = classification;
        this.fileMetaInf = fileMetaInf;
        this.expectedClassif = expectedClassif;
    }

    public double[] getDistribresult() {
        return distribresult;
    }

    public double getClassification() {
        return classification;
    }

    public FileMetaInf getFileMetaInf() {
        return fileMetaInf;
    }

    public double getExpectedClassif() {
        return expectedClassif;
    }
}
