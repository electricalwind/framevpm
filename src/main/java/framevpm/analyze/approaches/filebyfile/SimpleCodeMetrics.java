package framevpm.analyze.approaches.filebyfile;

import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.PerFileAnalysis;
import framevpm.analyze.model.Analysis;
import lu.jimenez.research.filemetrics.CodeMetrics;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class SimpleCodeMetrics extends PerFileAnalysis {

    private final static String NAME = "Simple Metrics";

    public SimpleCodeMetrics(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public Analysis analyseFile(String file, String fileContent, String hash) {

        CodeMetrics metrics = new CodeMetrics(fileContent, new ArrayList<>());
        Map<String, Serializable> objectMap = new HashMap<>();
        objectMap.put("loc", metrics.getLinesOfCode());
        objectMap.put("blankLines", metrics.getBlankLines());
        objectMap.put("preprocessorLines", metrics.getPreprocessorLines());
        objectMap.put("commentingLines", metrics.getCommentingLines());
        objectMap.put("commentDensity", metrics.commentDensity());
        objectMap.put("countDeclFunction", metrics.countDeclFunction());
        objectMap.put("countDeclVariable", metrics.countDeclvariable());
        return new Analysis(objectMap);
    }

    @Override
    public String getApproachName() {
        return NAME;
    }
}
