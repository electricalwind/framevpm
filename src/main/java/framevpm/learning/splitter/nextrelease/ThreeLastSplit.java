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

public class ThreeLastSplit extends ExperimentSplitter {
    public final static String NAME = "3LastReleaseGen";

    public ThreeLastSplit(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public List<Experiment> generateExperiment() throws IOException, ClassNotFoundException {
        List<Experiment> experiments = new LinkedList<>();

        String currentrelease;

        ReleaseAnalysis currentreleaseanaly;
        int counter = 0;
        LinkedList<LinkedHashMap<FileMetaInf, Map<String, Analysis>>> oldreleases = new LinkedList<>();
        LinkedHashMap<FileMetaInf, Map<String, Analysis>> training;
        LinkedHashMap<FileMetaInf, Map<String, Analysis>> testing;


        for (Map.Entry<String, Map<String, VulnerabilityInfo>> release : mapVuln.entrySet()) {
            if (release.getValue().size() > 0) {
                currentrelease = release.getKey();
                currentreleaseanaly = exporter.loadReleaseAnalysis(project, currentrelease);

                testing = new LinkedHashMap<>();

                for (Map.Entry<String, FileAnalysis> file : currentreleaseanaly.getFileAnalysisMap().entrySet()) {
                    Map<String, Analysis> analysis = file.getValue().getOriginal();
                    VulnerabilityInfo vulnerabilityInfo = release.getValue().getOrDefault(file.getKey(), null);
                    FileMetaInf metaInf = new FileMetaInf(release.getKey(), file.getKey(), file.getValue().getType(), vulnerabilityInfo);
                    testing.put(metaInf, analysis);
                }
                if (counter == 3) {
                    training = new LinkedHashMap<>();
                    training.putAll(oldreleases.get(0));
                    training.putAll(oldreleases.get(1));
                    training.putAll(oldreleases.get(2));
                    Experiment experiment = new Experiment(currentrelease, training, testing);
                    experiments.add(experiment);
                    oldreleases.removeFirst();
                    counter--;
                }
                oldreleases.add(testing);
                counter++;
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
