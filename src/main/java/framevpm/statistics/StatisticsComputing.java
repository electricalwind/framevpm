package framevpm.statistics;

import data7.model.vulnerability.Vulnerability;
import data7.project.CProjects;
import framevpm.ResourcesPathExtended;
import framevpm.statistics.vulnerabilities.BoxplotSever;
import framevpm.statistics.vulnerabilities.CSVExporter;
import framevpm.statistics.vulnerabilities.VulnerabilitiesStats;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StatisticsComputing {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/");

        VulnerabilitiesStats stats = new VulnerabilitiesStats(path);
        String project = CProjects.LINUX_KERNEL.getName();
        Map<String, List<Vulnerability>> mapRel = stats.getMapOfReleaseFor(project);
        Map<String, List<Double>> mapSev = stats.mapOfSeverity(mapRel);
        Map<String, Map<String, Integer>> mapKind = stats.mapOfKind(mapRel);

        CSVExporter csvExporter = new CSVExporter(path);
        csvExporter.kindCSV(project, mapKind);
        csvExporter.severCSV(project, mapSev);

    }
}
