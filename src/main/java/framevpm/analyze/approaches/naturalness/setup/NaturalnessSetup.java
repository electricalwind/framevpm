package framevpm.analyze.approaches.naturalness.setup;

import modelling.infrastructure.kylm.ngram.smoother.NgramSmoother;
import tokenizer.file.AbstractFileTokenizer;

public class NaturalnessSetup {
    private final AbstractFileTokenizer tokenizer;
    private final int n;
    private final int threshold;
    private final NgramSmoother smoother;

    public NaturalnessSetup(AbstractFileTokenizer tokenizer, int n, int threshold, NgramSmoother smoother){
        this.tokenizer = tokenizer;
        this.n = n;
        this.threshold = threshold;
        this.smoother = smoother;
    }

    public AbstractFileTokenizer getTokenizer() {
        return tokenizer;
    }

    public int getN() {
        return n;
    }

    public int getThreshold() {
        return threshold;
    }

    public NgramSmoother getSmoother() {
        return smoother;
    }

    @Override
    public String toString() {
        return "{" +
                "tokenizer=" + tokenizer.getType() +
                ", n=" + n +
                ", threshold=" + threshold +
                ", smoother=" + smoother.getAbbr() +
                '}';
    }
}
