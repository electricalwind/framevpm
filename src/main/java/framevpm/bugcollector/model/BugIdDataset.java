package framevpm.bugcollector.model;

import data7.project.Project;
import gitUtilitaries.GitActions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static data7.Utils.generateCommitOfInterest;

/**
 * BUg dataset implementation for project that mention bug id in their commits
 */
public class BugIdDataset extends BugDataset implements Serializable {
    private static final long serialVersionUID = 20180608L;


    public BugIdDataset(Project project) {
        super(project);
    }

    /**
     * Method to create or update the bug dataset
     *
     * @param mapOfBug  from the data7
     * @param mapOfVuln vulnerability to bug map
     * @param git       object to perform operation
     */
    public void updateDataset(Map<String, List<String>> mapOfBug, Map<String, List<String>> mapOfVuln, GitActions git) {
        mapOfBug.entrySet()
                .parallelStream()
                .forEach(
                        entry -> {
                            if (!mapOfVuln.containsKey(entry.getKey())) {
                                for (String hash : entry.getValue()) {
                                    if (!dataset.containsKey(hash)) {
                                        dataset.put(hash, generateCommitOfInterest(git, hash, true));
                                    }
                                }
                            } else {
                                for (String id : mapOfVuln.get(entry.getKey())) {
                                    if (mapOfBug.containsKey(id)) {
                                        for (String hash : mapOfBug.get(id)) {
                                            dataset.remove(hash);
                                        }
                                    }
                                }
                            }
                        });
    }

}