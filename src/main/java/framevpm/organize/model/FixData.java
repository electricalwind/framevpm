package framevpm.organize.model;

import java.io.Serializable;

/**
 *
 */
public class FixData implements Serializable {
    private static final long serialVersionUID = 20180611L;
    private final FileType typeFile;
    private String cwe;
    private final String hashBefore;
    private final String hashAfter;
    private final String before;
    private final String after;

    public FixData(FileType typeFile,String hashBefore, String hashAfter, String before, String after) {
        this.typeFile = typeFile;
        this.hashBefore = hashBefore;
        this.hashAfter = hashAfter;
        this.before = before;
        this.after = after;
        this.cwe = null;
    }


    public FileType getTypeFile() {
        return typeFile;
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }

    public String getCwe() {
        return cwe;
    }

    public String getHashBefore() {
        return hashBefore;
    }

    public String getHashAfter() {
        return hashAfter;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }
}
