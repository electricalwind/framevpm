package framevpm.learning.model;

import framevpm.learning.splitter.fileMeta.VulnerabilityInfo;
import framevpm.organize.model.FileType;

import java.io.Serializable;

public class FileMetaInf implements Serializable {

    private static final long serialVersionUID = 20180720L;

    private final String file;
    private final FileType type;
    private final VulnerabilityInfo vulnerabilityInfo;


    public FileMetaInf(String file, FileType type, VulnerabilityInfo vulnerabilityInfo) {
        this.file = file;
        this.type = type;
        this.vulnerabilityInfo = vulnerabilityInfo;
    }


    public String getFile() {
        return file;
    }

    public FileType getType() {
        return type;
    }

    public VulnerabilityInfo getVulnerabilityInfo() {
        return vulnerabilityInfo;
    }
}
