package framevpm.organize.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ProjectData implements Serializable {

    private static final long serialVersionUID = 20180608L;
    private final String project;
    private boolean vulndone;
    private boolean bugdone;
    private final Map<String, ReleaseData> releasesMap;

    public ProjectData(String project) {
        this.project = project;
        releasesMap = new HashMap<>();
        vulndone = false;
        bugdone = false;
    }



    public String getProject() {
        return project;
    }

    public Map<String, ReleaseData> getReleasesMap() {
        return releasesMap;
    }

    public ReleaseData getOrCreateRelease(String rel) {
        if (releasesMap.containsKey(rel)) {
            return releasesMap.get(rel);
        } else {
            ReleaseData releaseData = new ReleaseData(rel);
            releasesMap.put(rel, releaseData);
            return releaseData;
        }
    }

    public boolean isVulndone() {
        return vulndone;
    }

    public void setVulndone(boolean vulndone) {
        this.vulndone = vulndone;
    }

    public boolean isBugdone() {
        return bugdone;
    }

    public void setBugdone(boolean bugdone) {
        this.bugdone = bugdone;
    }

}