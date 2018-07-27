package framevpm.learning.splitter.nextrelease;

import framevpm.ResourcesPathExtended;
import framevpm.analyze.model.Analysis;
import framevpm.analyze.model.FileAnalysis;
import framevpm.analyze.model.ReleaseAnalysis;
import framevpm.learning.model.Experiment;
import framevpm.learning.model.FileMetaInf;
import framevpm.learning.splitter.ExperimentSplitter;
import framevpm.learning.splitter.fileMeta.VulnerabilityInfo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GeneralSplit extends ExperimentSplitter {
public final static  String NAME="nextReleaseGen";

    public GeneralSplit(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public List<Experiment> generateExperiment() throws IOException, ClassNotFoundException {
        List<Experiment> experiments = new LinkedList<>();

        String currentrelease;
        String oldrelease = null;
        ReleaseAnalysis currentreleaseanaly;

        LinkedHashMap<FileMetaInf, Map<String, Analysis>> training = null;
        LinkedHashMap<FileMetaInf, Map<String, Analysis>> testing;


        for (Map.Entry<String, Map<String, VulnerabilityInfo>> release : mapVuln.entrySet()) {
            if (release.getValue().size() > 0) {
                currentrelease = release.getKey();
                currentreleaseanaly = exporter.loadReleaseAnalysis(project, currentrelease);

                testing = new LinkedHashMap<>();

                for (Map.Entry<String, FileAnalysis> file : currentreleaseanaly.getFileAnalysisMap().entrySet()) {
                    Map<String, Analysis> analysis = file.getValue().getOriginal();
                    VulnerabilityInfo vulnerabilityInfo = release.getValue().getOrDefault(file.getKey(), null);
                    FileMetaInf metaInf = new FileMetaInf(file.getKey(), file.getValue().getType(), vulnerabilityInfo);
                    testing.put(metaInf, analysis);
                }
                if (training != null) {
                    Experiment experiment = new Experiment(oldrelease + "-to-" + currentrelease, training, testing);
                    experiments.add(experiment);
                }
                oldrelease = currentrelease;
                training = testing;
            }
        }

        exporter.saveExperiments(NAME, project, experiments);
        return experiments;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
