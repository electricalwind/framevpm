package framevpm.learning.splitter;

import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.learning.model.Experiment;
import framevpm.learning.splitter.fileMeta.Gatherer;
import framevpm.learning.splitter.fileMeta.VulnerabilityInfo;
import framevpm.project.ProjectInfoFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ExperimentSplitter {

    protected final ResourcesPathExtended pathExtended;
    protected final ExporterExtended exporter;
    protected final String project;
    protected Map<String, Map<String, VulnerabilityInfo>> mapVuln;

    public ExperimentSplitter(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        this.pathExtended = pathExtended;
        this.project = project;
        this.exporter = new ExporterExtended(pathExtended);
        this.mapVuln = exporter.loadProjectVulnData(project);
        if (mapVuln == null) {
            mapVuln = new Gatherer(pathExtended, project).gather();
        }
    }

    public abstract List<Experiment> generateExperiment() throws IOException, ClassNotFoundException;


    public abstract String getName();

}
