package framevpm.analyze.model;

import framevpm.organize.model.FileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileAnalysis implements Serializable {
    private static final long serialVersionUID = 20180613L;
    private final String file;
    private Analysis original;
    private FileType type;
    private  final List<FixAnalysis> fixes;

    public FileAnalysis(String file) {
        this.file = file;
        this.fixes = new ArrayList<>();
    }

    public void setOriginal(Analysis original) {
        this.original = original;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public Analysis getOriginal() {
        return original;
    }

    public FileType getType() {
        return type;
    }

    public List<FixAnalysis> getFixes() {
        return fixes;
    }
}
