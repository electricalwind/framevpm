package framevpm.learning.evaluate;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.RandomForest;

import java.util.ArrayList;
import java.util.List;

public class Classifiers {
    public static final Classifier createLogisticRegression() { return new Logistic();
    }

    public static final Classifier createRF() {
        return new RandomForest();
    }

    public static final Classifier createJ48() {
        return new RandomForest();
    }

    public static final Classifier createAda() {
        return new AdaBoostM1();
    }

    public static final Classifier createLinearSVM() {
        return new SMO();
    }

    public static final Classifier createKNear() {
        IBk ibk = new IBk();
        ibk.setKNN(5);
        return ibk;
    }

    public static final Classifier createMultiLayer() {
        return new MultilayerPerceptron();
    }

    public static final List<String> getClassifiers() {
        List<String> classifiers = new ArrayList<>();
        classifiers.add("Logistic");
        classifiers.add("RandomForest");
        //classifiers.add("J48");
        //classifiers.add("Ada");
        classifiers.add("SVM");
        //classifiers.add("KNear");
        //classifiers.add("MLPerceptron");
        return classifiers;
    }

    public static final Classifier getClassifier(String classif) {
        switch (classif) {
            case "Logistic":
                return createLogisticRegression();
            case "RandomForest":
                return createRF();
            case "J48":
                return createJ48();
            case "Ada":
                return createAda();
            case "SVM":
                return createLinearSVM();
            case "KNear":
                return createKNear();
            case "MLPerceptron":
                return createMultiLayer();
            default:
                return null;
        }
    }
}
