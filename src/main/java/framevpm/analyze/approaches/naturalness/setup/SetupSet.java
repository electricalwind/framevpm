package framevpm.analyze.approaches.naturalness.setup;

import modelling.infrastructure.kylm.ngram.smoother.NgramSmoother;
import modelling.main.kylm.SmootherFactory;
import tokenizer.file.AbstractFileTokenizer;
import tokenizer.file.CPPFileTokenizerFactory;

import java.util.LinkedList;
import java.util.List;

public class SetupSet {

    private static final int[] selectedn = new int[]{3,6};
    private static final NgramSmoother smoother = SmootherFactory.create("mkn");
    private static final AbstractFileTokenizer[] fileTokenizers = new AbstractFileTokenizer[]{CPPFileTokenizerFactory.createUTFTokenizer(),CPPFileTokenizerFactory.createLemmeTokenizer(),CPPFileTokenizerFactory.createASTTokenizer()};
    private static final int[] thresholds = new int[]{1,4};
    private static final SetupSet INSTANCE = new SetupSet();

    private final List<NaturalnessSetup> setups
            = new LinkedList<>();


    private SetupSet() {
            for (int size : selectedn) {
                for (AbstractFileTokenizer fileTokenizer: fileTokenizers){
                for (int threshold : thresholds) {
                    setups.add(new NaturalnessSetup(fileTokenizer,size,threshold,smoother));
                }
            }
        }
    }

    public static SetupSet instance() {
        return INSTANCE;
    }

    public Iterable<NaturalnessSetup> setups() {
        return setups;
    }


}
