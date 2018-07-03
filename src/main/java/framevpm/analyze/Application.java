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
        System.gc();
        runComplexity();
        System.gc();
        runCodeChurnDeveloper();
        System.gc();
        runIncludes();
        System.gc();
        runFunctionsCalls();
        System.gc();
        runBagOfWords();
        System.gc();
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
            NaturalnessSetup setup = setups.next();
            if (setup.getThreshold() == 1  || setup.getN() == 3) {
                pa = new OtherFilesNaturalness(pathExtended, project, setup).processFeatures();
            }
        }
        return pa;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/vpm/");
        Application application = new Application(path,"systemd");
        application.runAll();
        int k =0;
    }
}
