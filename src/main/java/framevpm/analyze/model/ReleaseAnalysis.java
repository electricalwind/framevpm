package framevpm.analyze.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReleaseAnalysis implements Serializable {
    private static final long serialVersionUID = 20180703L;
    private final String release;
    private final Map<String, FileAnalysis> fileAnalysisMap;


    public ReleaseAnalysis(String release) {
        this.release = release;
        this.fileAnalysisMap = new HashMap<>();
    }

    public FileAnalysis getOrCreateFileAnalysis(String file) {
        if (fileAnalysisMap.containsKey(file)) {
            return fileAnalysisMap.get(file);
        } else {
            FileAnalysis fileAnalysis = new FileAnalysis(file);
            fileAnalysisMap.put(file, fileAnalysis);
            return fileAnalysis;
        }
    }


    public String getRelease() {
        return release;
    }

    public Map<String, FileAnalysis> getFileAnalysisMap() {
        return fileAnalysisMap;
    }
}
