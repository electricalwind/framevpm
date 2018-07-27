package framevpm.learning;

import data7.project.CProjects;
import data7.project.Project;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.learning.approaches.Approach;
import framevpm.learning.approaches.codeMetrics.CodeMetricsApproach;
import framevpm.learning.approaches.ifc.FunctionCallsApproach;
import framevpm.learning.approaches.ifc.IncludesApproach;
import framevpm.learning.approaches.naturalness.PureNaturalness;
import framevpm.learning.approaches.textmining.BagOfWordsApproach;
import framevpm.learning.model.ApproachResult;
import framevpm.learning.model.Experiment;
import framevpm.learning.model.classmodel.ClassModel;
import framevpm.learning.model.classmodel.VulNotVul;
import framevpm.learning.splitter.ExperimentSplitter;
import framevpm.learning.splitter.nextrelease.GeneralSplit;
import framevpm.learning.splitter.nextrelease.ThreeLastSplit;

import java.io.IOException;
import java.util.List;

import static framevpm.learning.Classifiers.getClassifiers;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ResourcesPathExtended pathExtended = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/");
        ExporterExtended exporterExtended = new ExporterExtended(pathExtended);
        CSVExporter csvExporter = new CSVExporter(pathExtended);
        Project[] projects = new Project[]{
                // CProjects.OPEN_SSL,
                CProjects.WIRESHARK
                //, CProjects.LINUX_KERNEL
        };
        for (Project project : projects) {

            ClassModel model = new VulNotVul();

            ExperimentSplitter[] experimentSplitters = {new GeneralSplit(pathExtended, project.getName()), new ThreeLastSplit(pathExtended, project.getName())};


            for (ExperimentSplitter experimentSplitter : experimentSplitters) {
                List<Experiment> experimentList = new ExporterExtended(pathExtended).loadExperiments(project.getName(), experimentSplitter.getName());
                if (experimentList == null) {
                    experimentList = experimentSplitter.generateExperiment();
                }
                Approach[] approaches = {
                        new PureNaturalness(experimentList, model),
                        new CodeMetricsApproach(experimentList, model),
                        new IncludesApproach(experimentList, model),
                        new FunctionCallsApproach(experimentList, model),
                        new BagOfWordsApproach(experimentList, model)
                };

                for (Approach approach : approaches) {
                    for (String classifier : getClassifiers()) {
                        approach.prepareInstances();
                        runwithSmote(exporterExtended, csvExporter, project, model, experimentSplitter, approach, classifier, true);
                        runwithSmote(exporterExtended, csvExporter, project, model, experimentSplitter, approach, classifier, false);
                    }
                }
            }
        }

    }

    private static void runwithSmote(ExporterExtended exporterExtended, CSVExporter csvExporter, Project project, ClassModel model, ExperimentSplitter experimentSplitter, Approach approach, String classifier, boolean smote) throws IOException {
        ApproachResult result = approach.runWith(classifier, smote);
        exporterExtended.saveApproachResult(project.getName(), experimentSplitter.getName(), model.getName(), result);
        csvExporter.exportResultToCSV(project.getName(), experimentSplitter.getName(), model, result);

    }
}
