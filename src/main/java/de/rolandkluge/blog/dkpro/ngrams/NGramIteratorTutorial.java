package de.rolandkluge.blog.dkpro.ngrams;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.NGram;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramIterable;
import de.tudarmstadt.ukp.dkpro.core.ngrams.util.NGramStringIterable;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

/**
 * This class demonstrates how to work with n-grams using DKPro Core components.
 *
 * @author Roland Kluge
 *
 */
public class NGramIteratorTutorial
{
    public static void main(final String[] args)
    {
        final String sentence = "Mary gives John the apple.";
        runTokenBasedExample(sentence);
        runStringBasedExample(sentence);
    }

    private static void runTokenBasedExample(final String sentence)
    {
        try {
            final JCas jCas = JCasFactory.createJCas();
            jCas.setDocumentText(sentence);
            jCas.setDocumentLanguage("en");

            final AnalysisEngineDescription breakIteratorSegmenter = AnalysisEngineFactory
                    .createPrimitiveDescription(BreakIteratorSegmenter.class);
            SimplePipeline.runPipeline(jCas, breakIteratorSegmenter);

            final int n = 2;

            final Collection<Token> tokens = JCasUtil.select(jCas, Token.class);
            final NGramIterable<Token> ngrams = NGramIterable.create(tokens, n);
            final Iterator<NGram> ngramIterator = ngrams.iterator();

            System.out.print("Token-based bigrams: ");

            while (ngramIterator.hasNext()) {

                final NGram ngram = ngramIterator.next();

                if (JCasUtil.selectCovered(Token.class, ngram).size() == n) {
                    System.out.print(ngram.getCoveredText());

                    if (ngramIterator.hasNext()) {
                        System.out.print(", ");
                    }
                }
            }
            System.out.println();
        }
        catch (final UIMAException e) {
            e.printStackTrace();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void runStringBasedExample(final String sentence)
    {
        final String[] tokens = sentence.replace(".", " .").split("\\s");
        final Iterator<String> ngramIterator = new NGramStringIterable(tokens, 2, 2).iterator();

        System.out.print("String-based bigrams: ");
        while (ngramIterator.hasNext()) {

            final String ngram = ngramIterator.next();

            System.out.print(ngram);

            if (ngramIterator.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

}
