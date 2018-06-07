package framevpm.bugcollector.model;

import data7.model.change.Commit;
import data7.project.Project;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing the bug dataset
 */
public abstract class BugDataset implements Serializable {

    private static final long serialVersionUID = 20180608L;

    //bug dataset
    protected final Map<String, Commit> dataset;
    //project from the bug dataset
    private final Project project;

    protected BugDataset(Project project) {
        this.dataset = new ConcurrentHashMap<>();
        this.project = project;
    }


    public Map<String, Commit> getDataset() {
        return dataset;
    }

    public Project getProject() {
        return project;
    }
}
