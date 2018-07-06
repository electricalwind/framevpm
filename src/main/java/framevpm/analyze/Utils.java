package framevpm.analyze;

import data7.Exporter;
import framevpm.ExporterExtended;
import framevpm.analyze.model.ProjectAnalysis;
import framevpm.analyze.model.ProjectReleaseAnalysed;
import framevpm.analyze.model.ReleaseAnalysis;
import framevpm.project.CProjectsInfo;
import framevpm.project.ProjectInfoFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;

public class Utils {
    public static void transformProectAnalysisIntoProjectAnalyzed(ProjectAnalysis projectAnalysis, ExporterExtended exporter) throws IOException {
        Collection<String> releases = ProjectInfoFactory.retrieveProjectRelease(projectAnalysis.getProject()).values();
        ProjectReleaseAnalysed projectReleaseAnalysed = new ProjectReleaseAnalysed(projectAnalysis.getProject());
        for (String release :releases){
            ReleaseAnalysis releaseAnalysis =projectAnalysis.getOrCreateReleaseAnalysis(release);
            if(releaseAnalysis.getFileAnalysisMap().size()!=0){
                exporter.saveReleaseAnalysis(releaseAnalysis,projectAnalysis.getProject());
                projectReleaseAnalysed.getReleaseAnalyzed().add(release);
            }
        }
        exporter.saveProjectReleaseAnalysis(projectReleaseAnalysed);
    }
}
