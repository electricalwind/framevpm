package framevpm.releasebalancer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ReleaseData implements Serializable {
    private static final long serialVersionUID = 20180611L;
    private final String release;
    private final Map<String, FileData> fileMap;

    public ReleaseData(String release) {
        this.release = release;
        fileMap = new HashMap<>();
    }

    public FileData getOrCreateFile(String file) {
        if (fileMap.containsKey(file)) {
            return fileMap.get(file);
        } else {
            FileData fileData = new FileData(file);
            fileMap.put(file, fileData);
            return fileData;
        }
    }

    public FileData getFile(String file) {
        return fileMap.getOrDefault(file, null);
    }

    public String getRelease() {
        return release;
    }

    public Map<String, FileData> getFileMap() {
        return fileMap;
    }
}
