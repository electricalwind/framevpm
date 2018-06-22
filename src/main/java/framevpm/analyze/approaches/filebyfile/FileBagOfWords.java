package framevpm.analyze.approaches.filebyfile;

import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.PerFileAnalysis;
import framevpm.analyze.model.Analysis;
import miscUtils.StringAnalysis;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FileBagOfWords extends PerFileAnalysis {

    private final static String NAME = "Bag Of Word";

    public FileBagOfWords(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public Analysis analyseFile(String file, String fileContent, String hash) {
        Map<String, Integer> bagOfWords = StringAnalysis.slicingWord(StringAnalysis.contentWithoutComment(fileContent));
        Map<String, Serializable> objectMap = new HashMap<>(bagOfWords);
        return new Analysis(objectMap);

    }

    @Override
    public String getApproachName() {
        return NAME;
    }

}
