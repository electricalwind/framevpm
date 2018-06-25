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
import modelling.exception.TrainingFailedException;
import modelling.infrastructure.NgramModelKylmImpl;
import tokenizer.file.java.exception.UnparsableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class PreviousVersionNaturalness extends Analyze {
    private final static String NAME = "Previous Release Naturalness";
    private final NaturalnessSetup setup;
    private final ApproachAnalysis approachAnalysis;


    public PreviousVersionNaturalness(ResourcesPathExtended pathExtended, String project, NaturalnessSetup setup) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
        this.setup = setup;
        approachAnalysis = projectAnalysis.getOrCreateApproachAnalysis(getApproachName());

    }

    @Override
    public ProjectAnalysis processFeatures() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletionService<FileAnalysis> completionService = new ExecutorCompletionService(executor);
        System.out.println("Starting: " + getApproachName());
        try {
            List<Iterable<String>> tokenizedPreviousRelease = null;
            for (String release : releases) {
                ReleaseData releaseData = projectData.getOrCreateRelease(release);
                Map<String, Iterable<String>> tokenizedFiles = new HashMap<>();
                Map<String, String> files = loadVersion(release);
                for (Map.Entry<String, String> fileEntry : files.entrySet()) {
                    tokenizedFiles.put(fileEntry.getKey(), setup.getTokenizer().tokenize(fileEntry.getValue()));
                }
                if (releaseData.getFileMap().size() != 0) {
                    if (tokenizedPreviousRelease != null) {
                        ReleaseAnalysis releaseAnalysis = approachAnalysis.getOrCreateReleaseAnalysis(release);
                        NgramModel model = new NgramModelKylmImpl(setup.getN(), setup.getSmoother(), setup.getThreshold());
                        model.train(tokenizedPreviousRelease);
                        if (releaseAnalysis.getFileAnalysisMap().size() == 0) {
                            int count = 0;
                            for (Map.Entry<String, Iterable<String>> fileEntry : tokenizedFiles.entrySet()) {
                                completionService.submit(handleFile(releaseData, fileEntry, model));
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
                tokenizedPreviousRelease = new ArrayList<>(tokenizedFiles.values());
            }
        } catch (InterruptedException | UnparsableException | TrainingFailedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return projectAnalysis;
    }

    private Callable<FileAnalysis> handleFile(ReleaseData releaseData, Map.Entry<String, Iterable<String>> fileEntry, NgramModel model) {
        return () -> {
            FileData data = releaseData.getFile(fileEntry.getKey());
            return NaturalnessComputation.computeNaturalness(data, model, fileEntry.getKey(), fileEntry.getValue(), setup.getTokenizer());
        };
    }

    @Override
    public String getApproachName() {
        return NAME + setup;
    }
}
