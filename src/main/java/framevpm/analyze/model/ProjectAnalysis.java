package framevpm.analyze.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProjectAnalysis implements Serializable {
    private static final long serialVersionUID = 20180703L;
    private final String project;

    private final Map<String, ReleaseAnalysis> mapOfReleaseAnalysis;

    public ProjectAnalysis(String project) {
        this.project = project;
        mapOfReleaseAnalysis = new HashMap<>();
    }

    public String getProject() {
        return project;
    }


    public ReleaseAnalysis getOrCreateReleaseAnalysis(String release) {
        if (mapOfReleaseAnalysis.containsKey(release)) {
            return mapOfReleaseAnalysis.get(release);
        } else {
            ReleaseAnalysis approachAnalysis = new ReleaseAnalysis(release);
            mapOfReleaseAnalysis.put(release, approachAnalysis);
            return approachAnalysis;
        }
    }
}
