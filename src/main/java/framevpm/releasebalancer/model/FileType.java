package framevpm.releasebalancer.model;

import java.io.Serializable;

public enum FileType implements Serializable {

    Vulnerability,
    Buggy,
    BuggyHistory,
    VulnerableHistory,
    Clear
}