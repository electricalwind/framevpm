package framevpm.analyze.approaches;

import data7.Resources;
import framevpm.ResourcesPathExtended;
import framevpm.analyze.Analyze;
import framevpm.analyze.model.ApproachAnalysis;
import framevpm.analyze.model.FileAnalysis;
import framevpm.analyze.model.ProjectAnalysis;
import framevpm.analyze.model.ReleaseAnalysis;
import framevpm.organize.model.ProjectData;
import framevpm.organize.model.ReleaseData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextMining extends Analyze {

    private final static String name = "Text Mining";
    private final ApproachAnalysis approachAnalysis;

    public TextMining(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
        approachAnalysis = projectAnalysis.getOrCreateApproachAnalysis(name);
    }


    @Override
    public ProjectAnalysis processFeatures() {
        ExecutorService executor = Executors.newFixedThreadPool(Resources.NB_THREADS);
        CompletionService<FileAnalysis> completionService = new ExecutorCompletionService(executor);
        for (String release : releases) {
            ReleaseData releaseData = projectData.getOrCreateRelease(release);
            if (releaseData.getFileMap().size() != 0) {
                System.out.println("Starting: " + release);
                Map<String, String> files = loadVersion(release);
                /**ReleaseAnalysis releaseAnalysis = loadAnalyzeRelease(project, release);
                if (releaseAnalysis == null) {
                    Map<String, Iterable<String>> tokenizedFiles = new HashMap<>();
                    int count = 0;
                    for (Map.Entry<String, String> fileEntry : files.entrySet()) {
                        completionService.submit(new FileAnalysisCall(releaseExp.getFileExp(fileEntry.getKey()), release, fileEntry.getKey(), fileEntry.getValue(), git));
                        count++;
                        tokenizedFiles.put(fileEntry.getKey(), TOKENIZER.tokenize(fileEntry.getValue()));
                    }
                    Utils.saveAnalyzeRelease(handleResults(count, completionService, release), project);
                    Utils.saveTokenizedContent(project, release, tokenizedFiles);
                    NgramModel model = new NgramModelKylmImpl(N, SMOOTHER, THRESHOLD);
                    model.train(tokenizedFiles.values());
                    Utils.saveModel(project, release, model);

                    System.out.println("done first part rel: " + release);*/
                //}
            }

        }
        executor.shutdown();
        return null;
    }
}
