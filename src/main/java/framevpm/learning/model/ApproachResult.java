package framevpm.learning.model;

import java.io.Serializable;
import java.util.Map;

public class ApproachResult implements Serializable {
    private static final long serialVersionUID = 20180727L;
    private final String approach;

    private final boolean smote;

    private final String classifier;

    private final Map<String, Map<String,ExperimentResult>> resultMap;

    public ApproachResult(String approach, boolean smote, String classifier, Map<String, Map<String, ExperimentResult>> resultMap) {
        this.approach = approach;
        this.smote = smote;
        this.classifier = classifier;
        this.resultMap = resultMap;
    }

    public String getApproach() {
        return approach;
    }

    public boolean isSmote() {
        return smote;
    }

    public String getClassifier() {
        return classifier;
    }

    public Map<String, Map<String, ExperimentResult>> getResultMap() {
        return resultMap;
    }
}
