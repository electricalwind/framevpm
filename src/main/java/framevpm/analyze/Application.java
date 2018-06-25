package framevpm.analyze;

import data7.Exporter;
import data7.project.CProjects;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.filebyfile.*;
import framevpm.analyze.approaches.naturalness.OtherFilesNaturalness;
import framevpm.analyze.approaches.naturalness.PreviousVersionNaturalness;
import framevpm.analyze.approaches.naturalness.setup.NaturalnessSetup;
import framevpm.analyze.approaches.naturalness.setup.SetupSet;
import framevpm.analyze.model.ProjectAnalysis;

import java.io.IOException;
import java.util.Iterator;

public class Application {

    private final ResourcesPathExtended pathExtended;
    private final String project;

    public Application(ResourcesPathExtended pathExtended, String project) {
        this.pathExtended = pathExtended;
        this.project = project;
    }

    public ProjectAnalysis runAll() throws IOException, ClassNotFoundException {
        runSimpleMetrics();
        runComplexity();
        runCodeChurnDeveloper();

        runIncludes();
        runFunctionsCalls();

        runBagOfWords();

        runPreviousReleaseNaturalness();
        return runOtherFilesNaturalness();
    }


    public ProjectAnalysis runCodeChurnDeveloper() throws IOException, ClassNotFoundException {
        return new CodeChurnDeveloperMetrics(pathExtended, project).processFeatures();
    }

    public ProjectAnalysis runComplexity() throws IOException, ClassNotFoundException {
        return new ComplexityMetrics(pathExtended, project).processFeatures();
    }

    public ProjectAnalysis runSimpleMetrics() throws IOException, ClassNotFoundException {
        return new SimpleCodeMetrics(pathExtended, project).processFeatures();
    }

    public ProjectAnalysis runIncludes() throws IOException, ClassNotFoundException {
        return new FileIncludes(pathExtended, project).processFeatures();
    }

    public ProjectAnalysis runFunctionsCalls() throws IOException, ClassNotFoundException {
        return new FileFunctionCalls(pathExtended, project).processFeatures();
    }

    public ProjectAnalysis runBagOfWords() throws IOException, ClassNotFoundException {
        return new FileBagOfWords(pathExtended, project).processFeatures();
    }


    public ProjectAnalysis runPreviousReleaseNaturalness() throws IOException, ClassNotFoundException {
        Iterator<NaturalnessSetup> setups = SetupSet.instance().setups().iterator();
        ProjectAnalysis pa = null;
        while (setups.hasNext()) {
            pa = new PreviousVersionNaturalness(pathExtended, project, setups.next()).processFeatures();
        }
        return pa;
    }

    public ProjectAnalysis runOtherFilesNaturalness() throws IOException, ClassNotFoundException {
        Iterator<NaturalnessSetup> setups = SetupSet.instance().setups().iterator();
        ProjectAnalysis pa = null;
        while (setups.hasNext()) {
            pa = new OtherFilesNaturalness(pathExtended, project, setups.next()).processFeatures();
        }
        return pa;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/vpm/");
        /**System.out.println("Start Linux");
         Application application = new Application(path, CProjects.LINUX_KERNEL.getName());
         application.runAll();
         System.out.println("End Linux : " + (System.currentTimeMillis() - time));
         time = System.currentTimeMillis();*/
        System.out.println("Start SystemD");
        Application application = new Application(path, CProjects.SYSTEMD.getName());
        application.runAll();
        System.out.println("End SystemD : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        /**System.out.println("Start Wireshark");
        application = new Application(path, CProjects.WIRESHARK.getName());
        application.runAll();
        System.out.println("End Wireshark : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SSL");
        application = new Application(path, CProjects.OPEN_SSL.getName());
        application.runAll();
        System.out.println("End SSL : " + (System.currentTimeMillis() - time));*/
        /**ExporterExtended exporterExtended = new ExporterExtended(path);
        ProjectAnalysis projectAnalysis = exporterExtended.loadProjectAnalysis(CProjects.SYSTEMD.getName());
        int i = 0;*/
    }
}
