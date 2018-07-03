package framevpm.analyze.model;

import framevpm.organize.model.FileType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FixAnalysis implements Serializable {
    private static final long serialVersionUID = 20180703L;
    private final FileType type;
    private final Map<String,Analysis> before;
    private final Map<String,Analysis> after;
    private String cwe;
    private String cvss;

    public FixAnalysis(FileType type) {
        this.type = type;
        this.before = new HashMap<>();
        this.after = new HashMap<>();
        cvss = null;
        cwe = null;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }

    public FileType getType() {
        return type;
    }

    public Map<String, Analysis> getBefore() {
        return before;
    }

    public Map<String, Analysis> getAfter() {
        return after;
    }

    public String getCwe() {
        return cwe;
    }

    public String getCvss() {
        return cvss;
    }

    public void setCvss(String cvss) {
        this.cvss = cvss;
    }
}
