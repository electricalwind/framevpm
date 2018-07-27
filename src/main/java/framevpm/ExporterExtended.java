package framevpm;


import data7.Exporter;
import framevpm.analyze.model.*;
import framevpm.bugcollector.model.BugDataset;
import framevpm.learning.model.ApproachResult;
import framevpm.learning.model.Experiment;
import framevpm.organize.model.ProjectData;
import framevpm.learning.splitter.fileMeta.VulnerabilityInfo;

import java.io.*;
import java.util.List;
import java.util.Map;


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

    public void saveReleaseAnalysis(ReleaseAnalysis releaseAnalysis, String project) throws IOException {
        checkFolderDestination(resourcesPathExtended.getAnalysisPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getAnalysisPath() + project + "_" + releaseAnalysis.getRelease() + "-analyzedData.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(releaseAnalysis);
        out.flush();
        fos.close();
    }

    public ReleaseAnalysis loadReleaseAnalysis(String project, String release) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getAnalysisPath() + project + "_" + release + "-analyzedData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ReleaseAnalysis data = (ReleaseAnalysis) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveProjectReleaseAnalysis(ProjectReleaseAnalysed dataset) throws IOException {
        checkFolderDestination(resourcesPathExtended.getAnalysisPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getAnalysisPath() + dataset.getProject() + "-analyzedProRelData.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(dataset);
        out.flush();
        fos.close();
    }

    public ProjectReleaseAnalysed loadProjectReleaseAnalysis(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getAnalysisPath() + project + "-analyzedProRelData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ProjectReleaseAnalysed data = (ProjectReleaseAnalysed) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveProjectVulnData(String project, Map<String, Map<String, VulnerabilityInfo>> projectData) throws IOException {
        checkFolderDestination(resourcesPathExtended.getStatPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getStatPath() + project + "-VulData.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(projectData);
        out.flush();
        fos.close();
    }

    public Map<String, Map<String, VulnerabilityInfo>> loadProjectVulnData(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getStatPath() + project + "-VulData.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            Map<String, Map<String, VulnerabilityInfo>> data = (Map<String, Map<String, VulnerabilityInfo>>) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveExperiments(String split, String project, List<Experiment> experiments) throws IOException {
        checkFolderDestination(resourcesPathExtended.getExperimentPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getExperimentPath() + project + "-" + split + "-experiments.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(experiments);
        out.flush();
        fos.close();
    }

    public List<Experiment> loadExperiments(String project, String split) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getExperimentPath() + project + "-" + split + "-experiments.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            List<Experiment> data = (List<Experiment>) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }

    public void saveApproachResult(String project, String split, String model, ApproachResult approachResult) throws IOException {
        checkFolderDestination(resourcesPathExtended.getExperimentPath());
        RandomAccessFile raf = new RandomAccessFile(resourcesPathExtended.getExperimentPath() + project + "-" + split + "-" + model + "-" + approachResult.getApproach() + "-" + approachResult.getClassifier() + "-" + approachResult.isSmote() + "-result.obj", "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(approachResult);
        out.flush();
        fos.close();
    }

    public ApproachResult loadExperiments(String project, String split, String model, String approach, String classifier, boolean smote) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getExperimentPath() + project + "-" + split + "-" + model + "-" + approach + "-" + classifier + "-" + smote + "-result.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            ApproachResult data = (ApproachResult) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }
}
