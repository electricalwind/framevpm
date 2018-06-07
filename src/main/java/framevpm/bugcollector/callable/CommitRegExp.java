package framevpm.bugcollector.callable;

import data7.model.change.Commit;
import data7.project.Project;
import gitUtilitaries.GitActions;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static data7.Utils.generateCommitOfInterest;

public class CommitRegExp implements Callable<Commit> {

    private final Project project;
    private final RevCommit revCommit;
    private final GitActions git;

    public CommitRegExp(Project project, RevCommit revCommit, GitActions git) {
        this.revCommit = revCommit;
        this.project = project;
        this.git = git;
    }

    @Override
    public Commit call() {
        Pattern pattern = Pattern.compile("[.|\\r|\\n]*" + project.getPatchInCommitessageRegexp());
        Matcher m = pattern.matcher(revCommit.getFullMessage());
        if (m.find() && !revCommit.getFullMessage().contains("Merge") && !revCommit.getFullMessage().contains("Revert")) {
            return generateCommitOfInterest(git, revCommit, true);
        }
        return null;
    }
}