package framevpm.statistics.vulnerabilities;

import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;
import data7.project.ProjectFactory;
import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;
import framevpm.project.ProjectInfoFactory;

import java.io.IOException;
import java.util.*;

public class VulnerabilitiesStats {

    private final ResourcesPathExtended resourcesPathExtended;
    private final ExporterExtended exporterExtended;

    public VulnerabilitiesStats(ResourcesPathExtended resourcesPathExtended) {
        this.resourcesPathExtended = resourcesPathExtended;
        this.exporterExtended = new ExporterExtended(resourcesPathExtended);
    }

    public Map<String, List<Vulnerability>> getMapOfReleaseFor(String project) throws IOException, ClassNotFoundException {
        if (ProjectFactory.retrieveProjectInfo(project) == null) {
            throw new RuntimeException("Incorrect Project");
        }
        TreeMap<Long, String> releases = ProjectInfoFactory.retrieveProjectRelease(project);
        LinkedHashMap<String, List<Vulnerability>> mapOfRelease = new LinkedHashMap<>();
        for (String release : releases.values()) {
            mapOfRelease.put(release, new ArrayList<>());
        }
        Map<String, String> releasesCVE = ProjectInfoFactory.retrieveProjectReleaseCVE(project);

        Data7 data7 = exporterExtended.loadDataset(project);
        Collection<Vulnerability> vulnds = data7.getVulnerabilitySet().getVulnerabilityDataset().values();
        for (Vulnerability vuln : vulnds) {
            Set<String> versions = vuln.getVersions();
            Map<String, Commit> commits = vuln.getPatchingCommits();
            if (commits.size() > 0) {
                long timestamp = 0;
                for (Commit commit : commits.values()) {
                    if (commit.getTimestamp() > timestamp) timestamp = commit.getTimestamp();
                }
                List<String> versionsToAdd = lookForCorrespondingRelease(timestamp, versions, releases, releasesCVE);
                for (String version : versionsToAdd) {
                    mapOfRelease.get(version).add(vuln);
                }
            }
        }
        LinkedHashMap<String, List<Vulnerability>> maprelease = new LinkedHashMap<>();
        mapOfRelease.entrySet().stream().filter(stringListEntry -> stringListEntry.getValue().size() > 10).forEach(stringListEntry -> maprelease.put(stringListEntry.getKey(), stringListEntry.getValue()));
        return maprelease;
    }

    public Map<String, Map<String, Integer>> mapOfKind(Map<String, List<Vulnerability>> releaseMap) {
        Map<String, Map<String, Integer>> kindmap = new LinkedHashMap<>();
        for (Map.Entry<String, List<Vulnerability>> release : releaseMap.entrySet()) {
            Map<String, Integer> mapkind = new HashMap<>();
            for (Vulnerability vulnerability : release.getValue()) {
                String cwe = vulnerability.getCwe();
                if (cwe == null) {
                    cwe = "0";
                }
                mapkind.merge(vulnerability.getCwe(), 1, Integer::sum);
            }
            kindmap.put(release.getKey(), mapkind);
        }
        return kindmap;
    }


    public Map<String, List<Double>> mapOfSeverity(Map<String, List<Vulnerability>> releaseMap) {
        Map<String, List<Double>> severmap = new LinkedHashMap<>();
        for (Map.Entry<String, List<Vulnerability>> release : releaseMap.entrySet()) {
            List<Double> severList = new ArrayList<>();
            for (Vulnerability vulnerability : release.getValue()) {
                String cvss = vulnerability.getScore();
                if (cvss == null) {
                    cvss = "0";
                }
                severList.add(Double.parseDouble(cvss));
            }
            severmap.put(release.getKey(), severList);
        }
        return severmap;
    }

    public Map<String, Map<String, Integer>> mapfileVun(Map<String, List<Vulnerability>> releaseMap) {
        Map<String, Map<String, Integer>> fileVuln = new LinkedHashMap<>();
        for (Map.Entry<String, List<Vulnerability>> release : releaseMap.entrySet()) {
            Map<String, Integer> mapfile = new HashMap<>();
            for (Vulnerability vulnerability : release.getValue()) {
                Set<String> files = new HashSet<>();
                for (Commit commit : vulnerability.getPatchingCommits().values()) {
                    commit.getFixes().forEach(fileFix -> files.add(fileFix.getFileBefore().getFilePath()));
                }
                for (String file : files) {
                    mapfile.merge(file, 1, Integer::sum);
                }
            }
            fileVuln.put(release.getKey(), mapfile);
        }
        return fileVuln;
    }

    private List<String> lookForCorrespondingRelease(long timestamp, Set<String> versions, NavigableMap<Long, String> releases, Map<String, String> releasesCVE) {
        List<String> v = new ArrayList<>();
        Map.Entry<Long, String> release = releases.floorEntry(timestamp);
        if (release != null) {
            v.add(release.getValue());
            for (String version : versions) {
                if (releasesCVE.containsKey(version)) {
                    v.add(releasesCVE.get(version));
                }
            }
        }
        return v;
    }

}
