package framevpm.analyze.approaches.filebyfile;

import ast.ASTNode;
import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.PerFileAnalysis;
import framevpm.analyze.model.Analysis;
import lu.jimenez.research.filemetrics.CodeMetrics;
import lu.jimenez.research.filemetrics.global.GlobalASTFunctions;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static framevpm.Utils.mapOfCallsFunction;


public class FileFunctionCalls extends PerFileAnalysis {

    public final static String NAME = "Function Calls";

    public FileFunctionCalls(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public Analysis analyseFile(String file, String fileContent, String hash) {
        Map<String, Serializable> functionCalls = new HashMap<>();
        CodeMetrics ast = new CodeMetrics(fileContent,new ArrayList<>());
        Map<String,Integer> mapCalls  = frequencyOfCalls(mapOfCallsFunction(ast));
        functionCalls.putAll(mapCalls);
        return new Analysis(functionCalls);
    }

    @Override
    public String getApproachName() {
        return NAME;
    }

    /**
     * Function to return the map of calls in a c file
     *
     * @param map file to study
     * @return map of the function call and the number of times they were called
     */
    public Map<String, Integer> frequencyOfCalls(Map<String, List<String>> map) {
        Map<String, Integer> calls = new HashMap<>();
        map
                .values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(entry -> {
                    if (calls.containsKey(entry))
                        calls.put(entry, calls.get(entry) + 1);
                    else
                        calls.put(entry, 1);
                });
        return calls;
    }


}
