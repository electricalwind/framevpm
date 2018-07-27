package framevpm.analyze.approaches.filebyfile;

import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.PerFileAnalysis;
import framevpm.analyze.model.Analysis;
import framevpm.analyze.model.FileAnalysis;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileIncludes extends PerFileAnalysis {

    public final static String NAME = "Includes";

    public FileIncludes(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public Analysis analyseFile(String file, String fileContent, String hash) {
        Map<String, Serializable> includes = new HashMap<>();
        for (String include : includeFinder(fileContent)) {
            includes.put(include, true);
        }
        return new Analysis(includes);
    }

    @Override
    public String getApproachName() {
        return NAME;
    }

    /**
     * Function to retrieve all include in a c file
     *
     * @param doc file to study
     * @return the list of include
     */
    private static List<String> includeFinder(String doc) {
        List<String> includes = new ArrayList<>();
        String[] lines = doc.split("\n");
        for (String studiedline : lines) {
            //Multi Line Comment (Blocking Metri
            if (studiedline.contains("#include")) {
                studiedline = studiedline.replace("#include ", "");
                studiedline = studiedline.replace(" ", "");
                studiedline = studiedline.replace("\"", "");
                studiedline = studiedline.replace("<", "");
                studiedline = studiedline.replace(">", "");

                includes.add(studiedline);
            }
        }
        return includes;
    }
}
