package framevpm.learning;

import data7.project.CProjects;
import data7.project.Project;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.learning.approaches.Approach;
import framevpm.learning.approaches.codeMetrics.CodeMetricsApproach;
import framevpm.learning.approaches.ifc.FunctionCallsApproach;
import framevpm.learning.approaches.ifc.IncludesApproach;
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
            List<Experiment> experiments = new ExporterExtended(pathExtended).loadExperiments(project.getName(), ThreeLastSplit.NAME);
            if (experiments == null) {
                ExperimentSplitter split = new ThreeLastSplit(pathExtended, project.getName());
                experiments = split.generateExperiment();
            }
            ClassModel model = new VulNotVul();
            Approach[] approaches = {
                    new CodeMetricsApproach(experiments, model),
                    new IncludesApproach(experiments, model),
                    new FunctionCallsApproach(experiments, model),
                    new BagOfWordsApproach(experiments, model),
            };
            //
            for (Approach approach : approaches) {
                for (String classifier : getClassifiers()) {
                    ApproachResult smote = approach.runWith(classifier, true);
                    ApproachResult notSmote = approach.runWith(classifier, false);
                    exporterExtended.saveApproachResult(project.getName(), ThreeLastSplit.NAME, model.getName(), smote);
                    csvExporter.exportResultToCSV(project.getName(), ThreeLastSplit.NAME, model, smote);
                    exporterExtended.saveApproachResult(project.getName(), ThreeLastSplit.NAME, model.getName(), notSmote);
                    csvExporter.exportResultToCSV(project.getName(), ThreeLastSplit.NAME, model, notSmote);
                }
            }
        }

    }
}
