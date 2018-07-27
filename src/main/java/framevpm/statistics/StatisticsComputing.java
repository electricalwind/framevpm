package framevpm.statistics;

import data7.project.CProjects;
import framevpm.ResourcesPathExtended;
import framevpm.learning.splitter.fileMeta.Gatherer;

import java.io.IOException;

public class StatisticsComputing {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/");


        String project = CProjects.LINUX_KERNEL.getName();
        new Gatherer(path,project).gather();

        /**
         *  VulnerabilitiesStats stats = new VulnerabilitiesStats(path);
         * Map<String, List<Vulnerability>> mapRel = stats.getMapOfReleaseFor(project);
        Map<String, List<Double>> mapSev = stats.mapOfSeverity(mapRel);
        Map<String, Map<String, Integer>> mapKind = stats.mapOfKind(mapRel);

        CSVExporter csvExporter = new CSVExporter(path);
        csvExporter.kindCSV(project, mapKind);
        csvExporter.severCSV(project, mapSev);*/


    }
}
