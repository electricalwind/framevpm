package framevpm.statistics.vulnerabilities;

import com.opencsv.CSVWriter;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.statistics.vulnerabilities.project.ProjectKindFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data7.Utils.checkFolderDestination;

public class CSVExporter {

    private final String path;

    public CSVExporter(ResourcesPathExtended resourcesPathExtended) {
        this.path = resourcesPathExtended.getStatPath();
        checkFolderDestination(path);
    }

    public void kindCSV(String project, Map<String, Map<String, Integer>> mapKindRel) throws IOException {
        CSVWriter writerom = new CSVWriter(new FileWriter(new File(path + project + "-kind.csv"), false));
        writerom.writeNext(new String[]{"Release", "CWE", "Number of Vulnerability"});

        String[] cwes = ProjectKindFactory.retrieveProjectKind(project);
        if (cwes == null) throw new RuntimeException("project invalid");

        mapKindRel.forEach((release, mapKind) -> {
            Map<String, Integer> mapCWErel = new HashMap<>();
            for (String cwe : cwes) {
                mapCWErel.put(cwe, 0);
            }
            mapKind.forEach((kind, number) -> {
                if (mapCWErel.containsKey(kind)) {
                    mapCWErel.put(kind, mapCWErel.get(kind) + number);
                } else {
                    mapCWErel.put("Others", mapCWErel.get("Others") + number);
                }
            });

            mapCWErel.forEach((cwe, number) -> writerom.writeNext(new String[]{release, cwe, String.valueOf(number)}));
        });

        writerom.close();
    }

    public void severCSV(String project, Map<String, List<Double>> sever) throws IOException {
        CSVWriter writerom = new CSVWriter(new FileWriter(new File(path + project + "-sever.csv"), false));
        writerom.writeNext(new String[]{"Release", "CVSS"});
        sever.forEach((release, cvsss) -> cvsss.forEach(cvss -> writerom.writeNext(new String[]{release, String.valueOf(cvss)})));
        writerom.close();
    }
}
