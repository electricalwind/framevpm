package framevpm.bugcollector;

import data7.model.Data7;

import data7.project.CProjects;
import data7.project.Project;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.bugcollector.model.BugDataset;
import framevpm.bugcollector.model.BugIdDataset;
import framevpm.bugcollector.model.BugRegExpDataset;
import gitUtilitaries.GitActions;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static framevpm.Utils.listOfCommitsFromData7;


/**
 * Bug Collector Class
 */
public class BugCollector {

    private final ResourcesPathExtended resourcesPathExtended;
    private final ExporterExtended exporterExtended;


    public BugCollector(ResourcesPathExtended resourcesPathExtended) {
        this.resourcesPathExtended = resourcesPathExtended;
        this.exporterExtended = new ExporterExtended(resourcesPathExtended);
    }

    public BugDataset updateOrCreateBugDataset(String projectName) throws IOException, ClassNotFoundException {
        Data7 datasetOfVuln = exporterExtended.loadDataset(projectName);
        if (datasetOfVuln == null) {
            throw new RuntimeException("invalid Project");
        }
        Project project = datasetOfVuln.getProject();
        GitActions git = new GitActions(resourcesPathExtended.getGitPath() + projectName);


        BugDataset dataset = exporterExtended.loadBugDataset(project.getName());

        Set<String> commits = listOfCommitsFromData7(datasetOfVuln);
        if (dataset == null) {
            switch (project.getIndexOfBugIdinCommitMessage()) {
                case 0:
                    BugRegExpDataset datasetR = new BugRegExpDataset(project);
                    datasetR.updateDataset(commits, git);
                    exporterExtended.saveBugDataset(datasetR);
                    return datasetR;
                default:
                    BugIdDataset datasetI = new BugIdDataset(project);
                    datasetI.updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    exporterExtended.saveBugDataset(datasetI);
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
            exporterExtended.saveBugDataset(dataset);
            return dataset;
        }

    }


    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/");
        BugCollector bugCollector = new BugCollector(path);
        System.out.println("Start Linux");
        bugCollector.updateOrCreateBugDataset(CProjects.LINUX_KERNEL.getName());
        System.out.println("End Linux : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SystemD");
        bugCollector.updateOrCreateBugDataset(CProjects.SYSTEMD.getName());
        System.out.println("End SystemD : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start Wireshark");
        bugCollector.updateOrCreateBugDataset(CProjects.WIRESHARK.getName());
        System.out.println("End Wireshark : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SSL");
        bugCollector.updateOrCreateBugDataset(CProjects.OPEN_SSL.getName());
        System.out.println("End SSL : " + (System.currentTimeMillis() - time));
    }
}
