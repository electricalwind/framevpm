package framevpm;

import data7.ResourcesPath;

public class ResourcesPathExtended extends ResourcesPath {


    private final String bugDatasetPath;
    private final String organizeData;
    private final String analysisPath;
    private final String versionPath;
    private final String statPath;
    private final String experimentPath;

    public ResourcesPathExtended(String path) {
        super(path);
        this.bugDatasetPath = getSavingPath() + "bugdatasets/";
        this.organizeData = getSavingPath() + "organizeData/";
        this.analysisPath = getSavingPath() + "analysisData/";
        this.versionPath = getSavingPath() + "versions/";
        this.statPath = getSavingPath() + "stats/";
        this.experimentPath = getSavingPath() +"experiments/";
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

    public String getStatPath() { return statPath; }

    public String getExperimentPath() {
        return experimentPath;
    }
}

