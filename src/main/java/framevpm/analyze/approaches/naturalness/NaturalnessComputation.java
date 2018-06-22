package framevpm.analyze.approaches.naturalness;

import framevpm.analyze.model.Analysis;
import framevpm.analyze.model.FileAnalysis;
import framevpm.analyze.model.FixAnalysis;
import framevpm.organize.model.FileData;
import framevpm.organize.model.FixData;
import modelling.NgramModel;
import tokenizer.file.AbstractFileTokenizer;
import tokenizer.file.java.exception.UnparsableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NaturalnessComputation {

    public static FileAnalysis computeNaturalness(FileData data, NgramModel model, String file, Iterable<String> tokenizedFile, AbstractFileTokenizer tokenizer) {
        FileAnalysis fileAnalysis = new FileAnalysis(file);
        if (data != null) {
            for (FixData fixData : data.getFixes()) {
                Iterable<String> bf = null;
                Iterable<String> af = null;
                try {
                    bf = tokenizer.tokenize(fixData.getBefore());
                    af = tokenizer.tokenize(fixData.getAfter());
                } catch (IOException | UnparsableException e) {
                    e.printStackTrace();
                }
                if (af != null && bf != null) {
                    Analysis bef = naturalnessOf(bf, model);
                    Analysis aft = naturalnessOf(bf, model);
                    FixAnalysis fixAnalysis = new FixAnalysis(fixData.getTypeFile(), bef, aft);
                    if (fixData.getCwe() != null) {
                        fixAnalysis.setCwe(fixData.getCwe());
                    }
                    fileAnalysis.getFixes().add(fixAnalysis);
                }
            }
        }
        Analysis analysis = naturalnessOf(tokenizedFile, model);
        fileAnalysis.setOriginal(analysis);
        return fileAnalysis;
    }

    private static Analysis naturalnessOf(Iterable<String> file, NgramModel model) {
        Map<String, Serializable> map = new HashMap<>();
        map.put("cross-Entropy", model.crossEntropy(file));
        return new Analysis(map);
    }
}
