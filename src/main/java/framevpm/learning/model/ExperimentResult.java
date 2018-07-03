package framevpm.learning.model;

import java.io.Serializable;

public class ExperimentResult implements Serializable {
    private static final long serialVersionUID = 20180415L;
    double[] listOfResult;
    private final Result result;

    /**
     * Data class representing the result of a Binary classification experiment
     *
     * @property listOfResult list of the classification obtain at the end of the experiment
     * @property result scoring of the experiment
     */
    public ExperimentResult(double[] listOfResult, Result result) {
        this.listOfResult = listOfResult;
        this.result = result;
    }

    public double[] getListOfResult() {
        return listOfResult;
    }

    public Result getResult() {
        return result;
    }
}