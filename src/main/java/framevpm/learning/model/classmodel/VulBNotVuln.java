package framevpm.learning.model.classmodel;

import framevpm.organize.model.FileType;

import java.util.ArrayList;
import java.util.List;

public class VulBNotVuln  implements ClassModel {
    @Override
    public List<String> getClassList() {
        List<String> classList = new ArrayList<>();
        classList.add("Vulnerable");
        classList.add("Not Vulnerable");
        return classList;
    }

    @Override
    public String correspondingToTypeFile(FileType fileType) {
        switch (fileType) {
            case Clear:
            case BuggyHistory:
            case VulnerableHistory:
                return "Not Vulnerable";
            case Vulnerability:
            case Buggy:
                return "Vulnerable";
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public final static String NAME = "VulBnotvul";
}