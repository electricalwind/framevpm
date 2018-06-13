package framevpm.analyze.model;

import framevpm.organize.model.FileType;

import java.io.Serializable;

public class FixAnalysis implements Serializable {
    private static final long serialVersionUID = 20180613L;
    private final FileType type;
    private final Analysis before;
    private final Analysis after;
    private String cwe;

    public FixAnalysis(FileType type, Analysis before, Analysis after) {
        this.type = type;
        this.before = before;
        this.after = after;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }

    public FileType getType() {
        return type;
    }

    public Analysis getBefore() {
        return before;
    }

    public Analysis getAfter() {
        return after;
    }

    public String getCwe() {
        return cwe;
    }
}
