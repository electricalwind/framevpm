package framevpm.analyze.model;

import java.io.Serializable;
import java.util.Map;

public class Analysis implements Serializable {
    private static final long serialVersionUID = 20180613L;

    private final Map<String,Serializable> featureMap;

    public Analysis(Map<String, Serializable> featureMap) {
        this.featureMap = featureMap;
    }

    public Map<String, Serializable> getFeatureMap() {
        return featureMap;
    }
}
