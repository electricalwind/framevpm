package framevpm;


import data7.Exporter;
import framevpm.analyze.model.*;
import framevpm.bugcollector.model.BugDataset;
import framevpm.organize.model.ProjectData;

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
        FileOutputStream fos = new FileOutputStream(new RandomAccessFile(resourcesPathExtended.getBugDatasetPath() + dataset.getProject().getName() + "-bugdataset.obj", "rw").getFD());
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
        checkFolderDestination(resourcesPathExtended.getOrganizeData());
        FileOutputStream fos = new FileOutputStream(new RandomAccessFile(resourcesPathExtended.getOrganizeData() + dataset.getProject() + "-organizedData.obj", "rw").getFD());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataset);
        oos.close();
        fos.close();
    }

    public ProjectData loadProjectData(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getOrganizeData() + project + "-organizedData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ProjectData data = (ProjectData) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveProjectAnalysis(ProjectAnalysis dataset) throws IOException {
        checkFolderDestination(resourcesPathExtended.getAnalysisPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getAnalysisPath() + dataset.getProject() + "-analyzedData.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(dataset);
        out.flush();
        fos.close();
    }

    public ProjectAnalysis loadProjectAnalysis(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getAnalysisPath() + project + "-analyzedData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ProjectAnalysis data = (ProjectAnalysis) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }
}
