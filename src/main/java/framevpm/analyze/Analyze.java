package framevpm.analyze;


import data7.Utils;

import framevpm.ExporterExtended;
import framevpm.ResourcesPathExtended;

import framevpm.analyze.model.ProjectAnalysis;
import framevpm.organize.model.ProjectData;
import framevpm.project.ProjectInfoFactory;

import miscUtils.Misc;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static framevpm.analyze.Resources.FILTER_FILES_EXTENSION;
import static framevpm.analyze.Resources.FORBIDDEN;

@SuppressWarnings("Duplicates")
public abstract class Analyze {

    protected final String project;
    //protected final GitActions git;
    protected final Collection<String> releases;
    protected final ExporterExtended exporter;
    protected final ProjectData projectData;
    protected final ResourcesPathExtended path;
    protected ProjectAnalysis projectAnalysis;
    protected String releasePath;

    public Analyze(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        this.project = project;
        this.path = pathExtended;
        this.exporter = new ExporterExtended(pathExtended);
        TreeMap<Long, String> map = ProjectInfoFactory.retrieveProjectRelease(project);
        if (map == null) {
            throw new RuntimeException("invalid Project");
        }
        this.projectData = exporter.loadProjectData(project);
        if (projectData == null) {
            throw new RuntimeException("missing organized Data from organize package");
        }
        this.releases = map.values();

        this.releasePath = pathExtended.getVersionPath() + project + "/";
        Utils.checkFolderDestination(releasePath);
        if (new File(releasePath).list().length != releases.size()) {
            downloadAllVersionFor(project);
        }
        this.projectAnalysis = exporter.loadProjectAnalysis(project);
        if (projectAnalysis == null) {
            projectAnalysis = new ProjectAnalysis(project);
            exporter.saveProjectAnalysis(projectAnalysis);
        }
    }

    private void downloadAllVersionFor(String project) {
        String github = ProjectInfoFactory.retrieveProjectGithub(project);
        for (String version : releases) {
            String file = releasePath + version + ".tar.gz";
            if (!new File(file).exists()) {
                Misc.downloadFromURL(github + "/archive/" + version + ".tar.gz", releasePath);
            }
        }
    }

    protected Map<String, String> loadVersion(String version) {
        Map<String, String> fileMap = new HashMap<>();
        try {
            TarArchiveInputStream tar = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(releasePath + version + ".tar.gz")));
            TarArchiveEntry currentEntry = tar.getNextTarEntry();
            BufferedReader br;
            while (currentEntry != null) {
                br = new BufferedReader(new InputStreamReader(tar)); // Read directly from tarInput
                String file = currentEntry.getName();
                int index = file.indexOf("/");
                if (index > 0) {
                    file = file.substring(index + 1);
                }
                if (file.matches(FILTER_FILES_EXTENSION) && !file.contains(FORBIDDEN)) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    fileMap.put(file, sb.toString());
                }
                currentEntry = tar.getNextTarEntry(); // You forgot to iterate to the next file
            }
        } catch (IOException e) {
            System.err.println("problem with tar");
        }
        return fileMap;
    }

    public abstract ProjectAnalysis processFeatures() throws IOException;

    public abstract String getApproachName();

}