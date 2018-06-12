package framevpm.bugcollector;

import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;
import data7.project.CProjects;
import data7.project.Project;
import framevpm.Utils;
import framevpm.bugcollector.model.BugDataset;
import framevpm.bugcollector.model.BugIdDataset;
import framevpm.bugcollector.model.BugRegExpDataset;
import framevpm.releasebalancer.project.CProjectsInfo;
import gitUtilitaries.GitActions;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


import static data7.Importer.updateOrCreateDatasetFor;
import static data7.Resources.PATH_TO_GIT;
import static framevpm.Utils.listOfCommitsFromData7;
import static framevpm.Utils.loadBugDataset;

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
                    git.close();
                    return datasetR;
                default:
                    BugIdDataset datasetI = new BugIdDataset(project);
                    datasetI.updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    git.close();
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
            git.close();
            return dataset;
        }

    }


    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
        /**long time = System.currentTimeMillis();
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
        System.out.println("End SSL : " + (System.currentTimeMillis() - time));*/
        BugDataset bd = loadBugDataset(CProjects.OPEN_SSL.getName());
        final int[] filefixes = {0};
        Map<String, Integer> files = new HashMap<>();
        for (Map.Entry<String, Commit> bug : bd.getDataset().entrySet()) {
                        bug.getValue().getFixes().forEach(fileFix -> {
                                    files.merge(fileFix.getFileAfter().getFilePath(), 1, Integer::sum);
                                    filefixes[0]++;
                                }
                        );
                    }

        Map<String, Integer> topTenfiles =
                files.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(12)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        int i =0;
    }
}
