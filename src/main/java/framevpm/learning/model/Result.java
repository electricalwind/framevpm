package framevpm.learning.model;

import java.io.Serializable;


public class Result implements Serializable {

    private static final long serialVersionUID = 20180415L;
    private final double[][] confusionMatrix;

    /**
     * Data class representing the result of an experiment with usual metrics
     *
     * @property fp False positives
     * @property fn False negatives
     * @property tn True negatives
     * @property tp True positives
     */
    public Result(double[][] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

    public double fp(int classIndex) {
        if (classIndex < confusionMatrix.length) {
            double fp = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                if (i != classIndex) {
                    fp += confusionMatrix[i][classIndex];
                }
            }
            return fp;
        } else {
            return Double.NaN;
        }
    }

    public double fn(int classIndex) {
        if (classIndex < confusionMatrix.length) {
            double fn = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                if (i != classIndex) {
                    fn += confusionMatrix[classIndex][i];
                }
            }
            return fn;
        } else {
            return Double.NaN;
        }
    }

    public double tn(int classIndex) {
        if (classIndex < confusionMatrix.length) {
            double tn = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                if (i != classIndex) {
                    tn += confusionMatrix[i][i];
                }
            }
            return tn;
        } else {
            return Double.NaN;
        }
    }

    public double tp(int classIndex) {
        if (classIndex < confusionMatrix.length)
            return confusionMatrix[classIndex][classIndex];
        else
            return Double.NaN;
    }

    /**
     * Method returnin the total number of element in the testing set
     */
    public double totalNumber() {
        double sum = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                sum += confusionMatrix[j][i];
            }
        }
        return sum;
    }

    /**
     * Method computing the recall of an experiment
     * i.e., the number of true positive over the total of the positives
     */
    public double recall(int classIndex) {
        double tp = tp(classIndex);
        double fn = fn(classIndex);
        return tp / (tp + fn);
    }

    /**
     * Method computing the precision of an experiment
     * i.e., the number of true positive over the number of result declared as positive
     */
    public double precision(int classIndex) {
        double tp = tp(classIndex);
        double fp = fp(classIndex);
        return tp / (tp + fp);
    }

    /**
     * Method computing the fmeasure of an experiment
     * i.e., a measure of the harmonic mean of Recall and Precision
     */
    double fmeasure(int classIndex) {
        double precision = precision(classIndex);
        double recall = recall(classIndex);
        return (2 * precision * recall) / (precision + recall);
    }

    /**
     * Method computing the elusion of an experiment
     * i.e., the number of positive that were not found over the total of declared as negative
     */
    double elusion(int classIndex) {
        double tn = tn(classIndex);
        double fn = fn(classIndex);
        return fn / (fn + tn);
    }

    /**
     * Method computing the fallout of an experiment
     * i.e., number of declared wrongly as positive over the total of negative
     */
    double fallout(int classIndex) {
        double tn = tn(classIndex);
        double fp = fp(classIndex);
        return fp / (tn + fp);
    }

    /**
     * Method computing the accuracy of an experiment
     * i.e., number of correctly classify overall
     */
    double accuracy() {
        double right = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            right += confusionMatrix[i][i];
        }

        return right / totalNumber();
    }

    /**
     * Method computing the prevalence of an experiment
     * i.e., the number of elements to find as positive overall
     * this can be computed before laaunching the experiment in fact
     */
    double prevalence(int classIndex) {
        double classCount = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            classCount += confusionMatrix[classIndex][i];
        }
        return classCount / totalNumber();
    }

    /**
     * Method computing the true Negative rate of an experiment
     * i.e., the number of correctly classify as false over the number of false
     */
    double trueNegativeRate(int classIndex) {
        double tn = tn(classIndex);
        double fp = fp(classIndex);
        return tn / (tn + fp);
    }

    /**
     * Method computing the Matthews correlation coefficient,
     * i.e., correlation coefficient between the observed and predicted binary classifications;
     * it returns a value between −1 and +1. A coefficient of +1 represents a perfect prediction,
     * 0 no better than random prediction and −1 indicates total disagreement between prediction and observation
     */
    double mcc(int classIndex) {
        double tn = tn(classIndex);
        double fn = fn(classIndex);
        double tp = tp(classIndex);
        double fp = fp(classIndex);
        return (tp * tn - fp * fn) / Math.sqrt(((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn)));
    }

}
