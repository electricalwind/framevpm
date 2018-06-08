package framevpm.bugcollector;

import data7.model.Data7;
import data7.project.CProjects;
import data7.project.Project;
import framevpm.Utils;
import framevpm.bugcollector.model.BugDataset;
import framevpm.bugcollector.model.BugIdDataset;
import framevpm.bugcollector.model.BugRegExpDataset;
import gitUtilitaries.GitActions;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;


import static data7.Importer.updateOrCreateDatasetFor;
import static data7.Resources.PATH_TO_GIT;
import static framevpm.Utils.listOfCommitsFromData7;

/**
 * Bug Collector Class
 */
public class BugCollector {

    private final Data7 datasetOfVuln;
    private final GitActions git;
    private final Project project;


    public BugCollector(Data7 datasetOfVuln) {
        this.datasetOfVuln = datasetOfVuln;
        this.project = datasetOfVuln.getProject();
        git = new GitActions(datasetOfVuln.getProject().getOnlineRepository(), PATH_TO_GIT + project);
    }

    public BugDataset updateOrCreateBugDataset() throws IOException, ClassNotFoundException {
        BugDataset dataset = Utils.loadBugDataset(project.getName());

        Set<String> commits = listOfCommitsFromData7(datasetOfVuln);
        if (dataset == null) {
            switch (project.getIndexOfBugIdinCommitMessage()) {
                case 0:
                    BugRegExpDataset datasetR = new BugRegExpDataset(project);
                    datasetR.updateDataset(commits, git);
                    return datasetR;
                default:
                    BugIdDataset datasetI = new BugIdDataset(project);
                    datasetI.updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    return datasetI;
            }
        } else {
            switch (project.getIndexOfBugIdinCommitMessage()) {
                case 0:
                    ((BugRegExpDataset) dataset).updateDataset(commits, git);
                    break;
                default:
                    ((BugIdDataset) dataset).updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    break;
            }
            return dataset;
        }
    }


    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        System.out.println("Start Linux");
        Data7 dataset = updateOrCreateDatasetFor(CProjects.LINUX_KERNEL);
        BugCollector collector = new BugCollector(dataset);
        Utils.saveBugDataset(collector.updateOrCreateBugDataset());
        System.out.println("End Linux : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SystemD");
        dataset = updateOrCreateDatasetFor(CProjects.SYSTEMD);
        collector = new BugCollector(dataset);
        Utils.saveBugDataset(collector.updateOrCreateBugDataset());
        System.out.println("End SystemD : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start Wireshark");
        dataset = updateOrCreateDatasetFor(CProjects.WIRESHARK);
        collector = new BugCollector(dataset);
        Utils.saveBugDataset(collector.updateOrCreateBugDataset());
        System.out.println("End Wireshark : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SSL");
        dataset = updateOrCreateDatasetFor(CProjects.OPEN_SSL);
        collector = new BugCollector(dataset);
        Utils.saveBugDataset(collector.updateOrCreateBugDataset());
        System.out.println("End SSL : " + (System.currentTimeMillis() - time));
    }
}
