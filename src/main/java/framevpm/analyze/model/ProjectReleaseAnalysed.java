package framevpm.analyze.model;

import java.io.Serializable;
import java.util.*;

public class ProjectReleaseAnalysed implements Serializable {
    private static final long serialVersionUID = 20180706L;
    private final String project;

    private final Set<String> releaseAnalyzed;

    public ProjectReleaseAnalysed(String project) {
        this.project = project;
        releaseAnalyzed = new HashSet<>();
    }

    public String getProject() {
        return project;
    }

    public Set<String> getReleaseAnalyzed() {
        return releaseAnalyzed;
    }

}
