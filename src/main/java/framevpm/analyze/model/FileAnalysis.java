package framevpm.analyze.model;

import framevpm.organize.model.FileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileAnalysis implements Serializable {
    private static final long serialVersionUID = 20180703L;
    private final String file;
    private final Map<String,Analysis> original;
    private FileType type;
    private  final List<FixAnalysis> fixes;

    public FileAnalysis(String file) {
        this.file = file;
        this.original = new HashMap<>();
        this.fixes = new ArrayList<>();
    }


    public void setType(FileType type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public FileType getType() {
        return type;
    }

    public List<FixAnalysis> getFixes() {
        return fixes;
    }

    public Map<String, Analysis> getOriginal() {
        return original;
    }
}
