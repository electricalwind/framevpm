package framevpm.analyze.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProjectAnalysis implements Serializable {
    private static final long serialVersionUID = 20180613L;
    private final String project;

    private final Map<String, ApproachAnalysis> mapOfApproachAnalysis;

    public ProjectAnalysis(String project) {
        this.project = project;
        mapOfApproachAnalysis = new HashMap<>();
    }

    public String getProject() {
        return project;
    }


    public ApproachAnalysis getOrCreateApproachAnalysis(String approach) {
        if (mapOfApproachAnalysis.containsKey(approach)) {
            return mapOfApproachAnalysis.get(approach);
        } else {
            ApproachAnalysis approachAnalysis = new ApproachAnalysis(approach);
            mapOfApproachAnalysis.put(approach, approachAnalysis);
            return approachAnalysis;
        }
    }


}
