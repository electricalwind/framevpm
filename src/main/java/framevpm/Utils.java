package framevpm;

import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;
import framevpm.bugcollector.model.BugDataset;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static framevpm.Ressources.PATH_TO_SAVE_EXP;

public class Utils {

    public static void saveBugDataset(BugDataset dataset) throws IOException {
        FileOutputStream fos = new FileOutputStream(PATH_TO_SAVE_EXP + dataset.getProject() + "-bugdataset.obj", false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataset);
        oos.close();
        fos.close();
    }

    public static BugDataset loadBugDataset(String project) throws IOException, ClassNotFoundException {
        File file = new File(PATH_TO_SAVE_EXP + project + "-bugdataset.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            BugDataset data = (BugDataset) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }


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
