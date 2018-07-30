package framevpm.learning.model.classmodel;

import framevpm.organize.model.FileType;

import java.util.ArrayList;
import java.util.List;

public class VulBugClear implements ClassModel {
    @Override
    public List<String> getClassList() {
        List<String> classList = new ArrayList<>();
        classList.add("Vulnerable");
        classList.add("Buggy");
        classList.add("Clear");
        return classList;
    }

    @Override
    public String correspondingToTypeFile(FileType fileType) {
        switch (fileType) {
            case Buggy:
                return "Buggy";
            case Clear:
            case BuggyHistory:
            case VulnerableHistory:
                return "Clear";
            case Vulnerability:
                return "Vulnerable";
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public final static String NAME = "VulBugClear";

}