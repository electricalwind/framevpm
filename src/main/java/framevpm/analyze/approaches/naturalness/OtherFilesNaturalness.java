package framevpm.analyze.approaches.naturalness;

import data7.Resources;
import framevpm.ResourcesPathExtended;
import framevpm.analyze.Analyze;
import framevpm.analyze.approaches.naturalness.setup.NaturalnessSetup;
import framevpm.analyze.model.ApproachAnalysis;
import framevpm.analyze.model.FileAnalysis;
import framevpm.analyze.model.ProjectAnalysis;
import framevpm.analyze.model.ReleaseAnalysis;
import framevpm.organize.model.FileData;
import framevpm.organize.model.ReleaseData;
import modelling.NgramModel;
import modelling.infrastructure.NgramModelKylmImpl;
import tokenizer.file.java.exception.UnparsableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class OtherFilesNaturalness extends Analyze {
    private final static String NAME = "Other Files Naturalness";
    private final NaturalnessSetup setup;
    private final ApproachAnalysis approachAnalysis;

    public OtherFilesNaturalness(ResourcesPathExtended pathExtended, String project, NaturalnessSetup setup) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
        this.setup = setup;
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
                    Map<String, Iterable<String>> tokenizedFiles = new HashMap<>();
                    Map<String, String> files = loadVersion(release);
                    for (Map.Entry<String, String> fileEntry : files.entrySet()) {
                        tokenizedFiles.put(fileEntry.getKey(), setup.getTokenizer().tokenize(fileEntry.getValue()));
                    }
                    System.out.println("Starting: " + release);
                    ReleaseAnalysis releaseAnalysis = approachAnalysis.getOrCreateReleaseAnalysis(release);
                    if (releaseAnalysis.getFileAnalysisMap().size() == 0) {
                        int count = 0;
                        for (Map.Entry<String, Iterable<String>> fileEntry : tokenizedFiles.entrySet()) {
                            completionService.submit(handleFile(releaseData, fileEntry, tokenizedFiles));
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
        } catch (InterruptedException | UnparsableException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return projectAnalysis;
    }

    private Callable<FileAnalysis> handleFile(ReleaseData releaseData, Map.Entry<String, Iterable<String>> fileEntry, Map<String, Iterable<String>> tokenizedFiles) {
        return () -> {
            List<Iterable<String>> training = tokenizedFiles.entrySet().stream().filter(stringIterableEntry -> !stringIterableEntry.getKey().equals(fileEntry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
            NgramModel model = new NgramModelKylmImpl(setup.getN(), setup.getSmoother(), setup.getThreshold());
            model.train(training);
            FileData data = releaseData.getFile(fileEntry.getKey());
            return NaturalnessComputation.computeNaturalness(data, model, fileEntry.getKey(), fileEntry.getValue(), setup.getTokenizer());
        };
    }

    @Override
    public String getApproachName() {
        return NAME + setup;
    }
}
