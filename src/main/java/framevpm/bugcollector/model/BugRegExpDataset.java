package framevpm.bugcollector.model;

import data7.importer.cve.processing.git.CommitRegexp;
import data7.model.change.Commit;
import data7.project.Project;
import framevpm.bugcollector.callable.CommitRegExp;
import gitUtilitaries.GitActions;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

import static data7.Resources.NB_THREADS;

/**
 * Bug dataset in case there is no mention to bug id in commit, in this case keywords are used
 */
public class BugRegExpDataset extends BugDataset implements Serializable {
    private static final long serialVersionUID = 20180608L;

    // already processed commits for further update
    private final Set<String> alreadyprocessed;


    public BugRegExpDataset(Project project) {
        super(project);
        this.alreadyprocessed = ConcurrentHashMap.newKeySet();
    }

    /**
     * method to update or create a dataset
     * @param vulnCommits list of commits used for vuln patch
     * @param git git object
     */
    public void updateDataset(Set<String> vulnCommits, GitActions git) {
        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        int errornb = 0;
        try {
            CompletionService<Commit> completionService = new ExecutorCompletionService(executor);
            int count = 0;
            Iterator<RevCommit> revCommitIterator = git.getGit().log().all().call().iterator();
            while (revCommitIterator.hasNext()) {
                RevCommit revCommit = revCommitIterator.next();
                String hash = revCommit.getName();
                if (!vulnCommits.contains(hash)) {
                    if (!alreadyprocessed.contains(hash)) {
                        alreadyprocessed.add(hash);
                        CommitRegExp commitRegexp = new CommitRegExp(this.getProject(), revCommit, git);
                        completionService.submit(commitRegexp);
                        count++;
                    }
                }else {
                    dataset.remove(hash);
                }
            }
            int received = 0;
            while (received < count) {
                Future<Commit> fut = completionService.take();
                try {
                    Commit result = fut.get();
                    if (result != null) {
                        dataset.put(result.getHash(), result);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    errornb++;
                } finally {
                    received++;
                }
            }

        } catch (InterruptedException | GitAPIException | IOException e) {
            e.printStackTrace();
        } finally {
            System.err.println("error : " + errornb);
            executor.shutdown();
        }
    }
}