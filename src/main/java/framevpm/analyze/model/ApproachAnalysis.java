package framevpm.analyze.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ApproachAnalysis implements Serializable {
    private static final long serialVersionUID = 20180613L;
    private final String approach;

    private final Map<String,ReleaseAnalysis> mapOfReleaseAnalysis;

    public ApproachAnalysis(String approach) {
        this.approach = approach;
        mapOfReleaseAnalysis = new HashMap<>();
    }

    public String getApproach() {
        return approach;
    }


    public ReleaseAnalysis getOrCreateReleaseAnalysis(String rel){
        if (mapOfReleaseAnalysis.containsKey(rel)) {
            return mapOfReleaseAnalysis.get(rel);
        } else {
            ReleaseAnalysis releaseExp = new ReleaseAnalysis(rel);
            mapOfReleaseAnalysis.put(rel, releaseExp);
            return releaseExp;
        }
    }


}
