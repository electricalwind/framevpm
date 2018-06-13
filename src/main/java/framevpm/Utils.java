package framevpm;

import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;
import framevpm.bugcollector.model.BugDataset;
import framevpm.releasebalancer.model.ProjectData;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static data7.Utils.checkFolderDestination;


public class Utils {




    public static Set<String> listOfCommitsFromData7(Data7 data7) {
        Set<String> commits = new HashSet<>();
        for (Map.Entry<String, Vulnerability> vuln : data7.getVulnerabilitySet().getVulnerabilityDataset().entrySet()) {
            if (vuln.getValue().getPatchingCommits().size() > 0) {
                for (Map.Entry<String, Commit> comm : vuln.getValue().getPatchingCommits().entrySet()) {
                    commits.add(comm.getKey());
                }

            }
        }
        return commits;
    }
}
