package framevpm.organize.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileData implements Serializable {
    private static final long serialVersionUID = 20180611L;
    private final String filePath;
    private FileType typeFile;
    private final List<FixData> fixes;

    public FileData(String filePath) {
        this.filePath = filePath;
        fixes = new ArrayList<>();
        typeFile = FileType.Clear;
    }

    public void setTypeFile(FileType typeFile) {
        this.typeFile = typeFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public FileType getTypeFile() {
        return typeFile;
    }

    public List<FixData> getFixes() {
        return fixes;
    }
}
