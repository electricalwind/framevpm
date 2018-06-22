package framevpm;

import ast.ASTNode;
import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;
import difflib.DiffUtils;
import difflib.Patch;
import framevpm.analyze.model.Analysis;
import framevpm.analyze.model.FileAnalysis;
import framevpm.analyze.model.FixAnalysis;
import framevpm.organize.model.FileData;
import framevpm.organize.model.FixData;
import lu.jimenez.research.filemetrics.CodeMetrics;
import lu.jimenez.research.filemetrics.global.GlobalASTFunctions;
import modelling.NgramModel;
import tokenizer.file.AbstractFileTokenizer;
import tokenizer.file.java.exception.UnparsableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class Utils {

    public static Set<String> listOfCommitsFromData7(Data7 data7) {
        Set<String> commits = new HashSet<>();
        for (Map.Entry<String, Vulnerability> vuln : data7.getVulnerabilitySet().getVulnerabilityDataset().entrySet()) {
            if (vuln.getValue().getPatchingCommits().size() > 0) {
                for (Map.Entry<String, Commit> comm : vuln.getValue().getPatchingCommits().entrySet()) {
                    commits.add(comm.getKey());
                }

            }
        }
        return commits;
    }

    public static Map<String, List<String>> mapOfCallsFunction(CodeMetrics doc) {
        List<ASTNode> nodes = doc.getListofNode();
        return GlobalASTFunctions
                .mapOfCallMadeByFunctions(nodes);
    }


}
