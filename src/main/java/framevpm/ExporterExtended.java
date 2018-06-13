package framevpm;

import data7.Exporter;
import framevpm.bugcollector.model.BugDataset;
import framevpm.releasebalancer.model.ProjectData;

import java.io.*;

import static data7.Utils.checkFolderDestination;

public class ExporterExtended extends Exporter {

    private final ResourcesPathExtended resourcesPathExtended;

    public ExporterExtended(ResourcesPathExtended resourcesPathExtended) {
        super(resourcesPathExtended);
        this.resourcesPathExtended = resourcesPathExtended;
    }

    public void saveBugDataset(BugDataset dataset) throws IOException {
        checkFolderDestination(resourcesPathExtended.getBugDatasetPath());
        FileOutputStream fos = new FileOutputStream(resourcesPathExtended.getBugDatasetPath() + dataset.getProject().getName() + "-bugdataset.obj", false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataset);
        oos.close();
        fos.close();
    }

    public BugDataset loadBugDataset(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getBugDatasetPath() + project + "-bugdataset.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            BugDataset data = (BugDataset) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveProjectData(ProjectData dataset) throws IOException {
        checkFolderDestination(resourcesPathExtended.getReleaseDataPath());
        FileOutputStream fos = new FileOutputStream(resourcesPathExtended.getReleaseDataPath() + dataset.getProject() + "-perReleaseData.obj", false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataset);
        oos.close();
        fos.close();
    }

    public ProjectData loadProjectData(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getReleaseDataPath() + project + "-perReleaseData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ProjectData data = (ProjectData) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }
}
