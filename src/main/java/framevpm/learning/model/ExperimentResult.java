package framevpm.learning.model;

import java.io.Serializable;

public class ExperimentResult implements Serializable {

    private static final long serialVersionUID = 20180415L;

    private final double[] distribresult;
    private final double classification;
    private final FileMetaInf fileMetaInf;

    public ExperimentResult(double[] distribresult, double classification, FileMetaInf fileMetaInf) {
        this.distribresult = distribresult;
        this.classification = classification;
        this.fileMetaInf = fileMetaInf;
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
}
