package framevpm;

import data7.ResourcesPath;

public class ResourcesPathExtended extends ResourcesPath {


    private final String bugDatasetPath;
    private final String organizeData;
    private final String analysisPath;
    private final String versionPath;

    public ResourcesPathExtended(String path) {
        super(path);
        this.bugDatasetPath = getSavingPath() + "bugdatasets/";
        this.organizeData = getSavingPath() + "organizeData/";
        this.analysisPath = getSavingPath() + "analysisData/";
        this.versionPath = getSavingPath() + "versions/";
    }

    public String getBugDatasetPath() {
        return bugDatasetPath;
    }

    public String getOrganizeData() {
        return organizeData;
    }

    public String getAnalysisPath() {
        return analysisPath;
    }

    public String getVersionPath() {
        return versionPath;
    }
}

