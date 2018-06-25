package framevpm.analyze.approaches;

import data7.Resources;
import framevpm.ResourcesPathExtended;
import framevpm.analyze.Analyze;
import framevpm.analyze.model.*;
import framevpm.organize.model.FileData;
import framevpm.organize.model.FileType;
import framevpm.organize.model.FixData;
import framevpm.organize.model.ReleaseData;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public abstract class PerFileAnalysis extends Analyze {


    private final ApproachAnalysis approachAnalysis;

    public PerFileAnalysis(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
        approachAnalysis = projectAnalysis.getOrCreateApproachAnalysis(getApproachName());
    }

    @Override
    public ProjectAnalysis processFeatures() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(Resources.NB_THREADS);
        CompletionService<FileAnalysis> completionService = new ExecutorCompletionService(executor);
        System.out.println("Starting: " + getApproachName());
        try {
            for (String release : releases) {
                ReleaseData releaseData = projectData.getOrCreateRelease(release);
                if (releaseData.getFileMap().size() != 0) {
                    System.out.println("Starting: " + release);
                    Map<String, String> files = loadVersion(release);
                    ReleaseAnalysis releaseAnalysis = approachAnalysis.getOrCreateReleaseAnalysis(release);
                    if (releaseAnalysis.getFileAnalysisMap().size() == 0) {
                        int count = 0;
                        for (Map.Entry<String, String> fileEntry : files.entrySet()) {

                            completionService.submit(handleFile(releaseData, fileEntry, release));
                            count++;
                        }
                        int received = 0;
                        while (received < count) {
                            Future<FileAnalysis> fut = completionService.take();
                            try {
                                FileAnalysis result = fut.get();
                                if (result != null) {
                                    releaseAnalysis.getFileAnalysisMap().put(result.getFile(), result);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } finally {
                                received++;
                                System.out.println(received + "/" + count);
                            }
                        }
                        System.out.println("done rel: " + release);
                        exporter.saveProjectAnalysis(projectAnalysis);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return projectAnalysis;
    }

    public Callable<FileAnalysis> handleFile(ReleaseData releaseData, Map.Entry<String, String> fileToAnalyze, String release) {
        return () -> {
            String file = fileToAnalyze.getKey();
            FileAnalysis fa = new FileAnalysis(file);
            FileData fileData = releaseData.getFile(file);
            if (fileData != null) {
                fa.setType(fileData.getTypeFile());
                for (FixData fixExp : fileData.getFixes()) {
                    Analysis bef = analyseFile(file, fixExp.getBefore(), fixExp.getHashBefore());
                    Analysis af = analyseFile(file, fixExp.getAfter(), fixExp.getHashAfter());
                    FixAnalysis fixAnalysis = new FixAnalysis(fixExp.getTypeFile(), bef, af);
                    if (fixExp.getCwe() != null) {
                        fixAnalysis.setCwe(fixExp.getCwe());
                    }
                    fa.getFixes().add(fixAnalysis);
                }
            } else {
                fa.setType(FileType.Clear);
            }
            fa.setOriginal(analyseFile(file, fileToAnalyze.getValue(), release));
            return fa;
        };
    }

    public abstract Analysis analyseFile(String file, String fileContent, String hash);

}
