package framevpm;

import data7.ResourcesPath;

public class ResourcesPathExtended extends ResourcesPath {


    private final String bugDatasetPath;
    private final String releaseDataPath;

    public ResourcesPathExtended(String path) {
        super(path);
        this.bugDatasetPath = getSavingPath() + "bugdatasets/";
        this.releaseDataPath = getSavingPath() + "perReleaseData/";
    }

    public String getBugDatasetPath() {
        return bugDatasetPath;
    }

    public String getReleaseDataPath() {
        return releaseDataPath;
    }
}

