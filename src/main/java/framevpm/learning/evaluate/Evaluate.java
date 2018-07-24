package framevpm.learning.evaluate;

import framevpm.learning.model.ExperimentResult;
import framevpm.learning.model.Result;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.Random;

public class Evaluate {
    /**
     * Function to perform K fold cross validation on an instances and returning the result score
     *
     * @param instances  instances gathering all elements
     * @param classifier weka classifier previously configurated
     * @param k          number of fold
     * @return Result of the experiment confusion matrix
     */
    public final static Result kfold(Instances instances, Classifier classifier, int k) throws Exception {
        Evaluation eval = new Evaluation(instances);
        eval.crossValidateModel(classifier, instances, k, new Random(System.currentTimeMillis()));
        double[][] confusionMatrix = eval.confusionMatrix();
        System.out.println(eval.toSummaryString());
        Result score = new Result(confusionMatrix);
        return score;
    }


    /**
     * Function to perform a Machine learning experiment using a training and a testing set
     *
     * @param traininginstances instances gathering training elements
     * @param testinginstances  instances gathering testing elements
     * @param classifier        weka classifier previously configurated
     */
    public final static ExperimentResult experiment2Instances(Instances traininginstances, Instances testinginstances, Classifier classifier) throws Exception {
        long startTime = System.currentTimeMillis();
        classifier.buildClassifier(traininginstances);
        long endTime = System.currentTimeMillis();
        System.out.println("Training took " + (endTime - startTime) + " milliseconds");
        double[][] probresults = new double[testinginstances.size()][];
        int i =0;
        testinginstances.forEach(instance -> {
            try {
                probresults[i] = classifier.distributionForInstance(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Evaluation eval = new Evaluation(traininginstances);
        double[] resultlist = eval.evaluateModel(classifier, testinginstances);
        double[][] confusionMatrix = eval.confusionMatrix();
        ExperimentResult result = new ExperimentResult(probresults,resultlist, new Result(confusionMatrix));
        return result;
    }

}
