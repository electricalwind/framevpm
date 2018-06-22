package framevpm.analyze.approaches.filebyfile;

import framevpm.ResourcesPathExtended;
import framevpm.analyze.approaches.PerFileAnalysis;
import framevpm.analyze.model.Analysis;
import lu.jimenez.research.filemetrics.CodeMetrics;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static framevpm.Utils.mapOfCallsFunction;

public class ComplexityMetrics extends PerFileAnalysis {
    private final static String NAME = "Complexity";

    public ComplexityMetrics(ResourcesPathExtended pathExtended, String project) throws IOException, ClassNotFoundException {
        super(pathExtended, project);
    }

    @Override
    public Analysis analyseFile(String file, String fileContent, String hash) {

        CodeMetrics metrics = new CodeMetrics(fileContent, new ArrayList<>());
        Map<String, List<String>> mapCalls = mapOfCallsFunction(metrics);

        Map<String, Integer> nesting = metrics.maxNesting();
        int maxNestingMax = nesting.values().stream().mapToInt(i -> i).max().orElse(0);
        double maxNestingAvg = nesting.values().stream().mapToInt(i -> i).average().orElse(0);
        int maxNestingSum = nesting.values().stream().mapToInt(i -> i).sum();

        Map<String, Integer> fanIn = metrics.fanIn(mapCalls);
        int fanInMax = fanIn.values().stream().mapToInt(i -> i).max().orElse(0);
        double fanInAvg = fanIn.values().stream().mapToInt(i -> i).average().orElse(0);
        int fanInSum = fanIn.values().stream().mapToInt(i -> i).sum();

        Map<String, Integer> fanOut = metrics.fanOut(mapCalls, null);
        int fanOutMax = fanOut.values().stream().mapToInt(i -> i).max().orElse(0);
        double fanOutAvg = fanOut.values().stream().mapToInt(i -> i).average().orElse(0);
        int fanOutSum = fanOut.values().stream().mapToInt(i -> i).sum();


        Map<String, Integer> cc = metrics.cyclomaticComplexity();
        int ccMax = cc.values().stream().mapToInt(i -> i).max().orElse(0);
        double ccAvg = cc.values().stream().mapToInt(i -> i).average().orElse(0);
        int ccSum = cc.values().stream().mapToInt(i -> i).sum();

        Map<String, Integer> scc = metrics.strictCyclomaticComplexity();
        int sccMax = scc.values().stream().mapToInt(i -> i).max().orElse(0);
        double sccAvg = scc.values().stream().mapToInt(i -> i).average().orElse(0);
        int sccSum = scc.values().stream().mapToInt(i -> i).sum();


        Map<String, Integer> mcc = metrics.modifiedCyclomaticComplexity();
        int mccMax = mcc.values().stream().mapToInt(i -> i).max().orElse(0);
        double mccAvg = mcc.values().stream().mapToInt(i -> i).average().orElse(0);
        int mccSum = mcc.values().stream().mapToInt(i -> i).sum();

        Map<String, Integer> ec = metrics.essentialComplexity();
        int ecMax = ec.values().stream().mapToInt(i -> i).max().orElse(0);
        double ecAvg = ec.values().stream().mapToInt(i -> i).average().orElse(0);
        int ecSum = ec.values().stream().mapToInt(i -> i).sum();

        Map<String, Serializable> objectMap = new HashMap<>();
        objectMap.put("ccMax", ccMax);
        objectMap.put("ccAverage", ccAvg);
        objectMap.put("ccSum", ccSum);
        objectMap.put("sccMax", sccMax);
        objectMap.put("sccAverage", sccAvg);
        objectMap.put("sccSum", sccSum);
        objectMap.put("mccMax", mccMax);
        objectMap.put("mccAverage", mccAvg);
        objectMap.put("mccSum", mccSum);
        objectMap.put("ecMax", ecMax);
        objectMap.put("ecAverage", ecAvg);
        objectMap.put("ecSum", ecSum);

        objectMap.put("maxNestingMax", maxNestingMax);
        objectMap.put("maxNestingAverage", maxNestingAvg);
        objectMap.put("maxNestingSum", maxNestingSum);
        objectMap.put("fanInMax", fanInMax);
        objectMap.put("fanInAverage", fanInAvg);
        objectMap.put("fanInSum", fanInSum);
        objectMap.put("fanOutMax", fanOutMax);
        objectMap.put("fanOutAverage", fanOutAvg);
        objectMap.put("fanOutSum", fanOutSum);

        return new Analysis(objectMap);
    }

    @Override
    public String getApproachName() {
        return NAME;
    }
}
