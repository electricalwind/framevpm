package framevpm.learning.model;

import framevpm.learning.splitter.fileMeta.VulnerabilityInfo;
import framevpm.organize.model.FileType;

import java.io.Serializable;
import java.util.Objects;

public class FileMetaInf implements Serializable {

    private static final long serialVersionUID = 20180727L;

    private final String release;
    private final String file;
    private final FileType type;
    private final VulnerabilityInfo vulnerabilityInfo;


    public FileMetaInf(String release, String file, FileType type, VulnerabilityInfo vulnerabilityInfo) {
        this.release = release;
        this.file = file;
        this.type = type;
        this.vulnerabilityInfo = vulnerabilityInfo;
    }

    public String getRelease() {
        return release;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileMetaInf)) return false;
        FileMetaInf that = (FileMetaInf) o;
        return Objects.equals(getRelease(), that.getRelease()) &&
                Objects.equals(getFile(), that.getFile()) &&
                getType() == that.getType() &&
                Objects.equals(getVulnerabilityInfo(), that.getVulnerabilityInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRelease(), getFile(), getType(), getVulnerabilityInfo());
    }
}
