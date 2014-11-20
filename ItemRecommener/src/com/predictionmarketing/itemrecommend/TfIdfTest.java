
package com.predictionmarketing.itemrecommend;


import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;
import org.apache.mahout.vectorizer.tfidf.TFIDFConverter;
import org.apache.hadoop.*;
 
public class TfIdfTest {
 
    static String inputDir =  "input/";
    static String outputDir = "output/";
     
    public static void main(String args[]) throws Exception {
        Configuration configuration = new Configuration();
         
        FileSystem fs = FileSystem.get(configuration);
        Path sequencePath = new Path(outputDir, "sequence");
        Path tokenizedPath = new Path(outputDir, DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER );
 
        tokenizeDocuments(fs, configuration, sequencePath, tokenizedPath);
        processTfIdf(configuration, tokenizedPath);        
        readSequenceFile(fs, configuration, "wordcount", "part-r-00000", new LongWritable());
        readSequenceFile(fs, configuration, "tfidf/tfidf-vectors", "part-r-00000", new VectorWritable());
    }
     
    public static void readSequenceFile(FileSystem fs, Configuration configuration, String path, String file,
            Writable writable) 
            throws Exception {
        Path vectorsFolder = new Path( outputDir, path );
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path(vectorsFolder, file), configuration);
 
        Text key = new Text();
        Writable value = writable;
        while (reader.next(key, value)) {
            if ( writable instanceof LongWritable ) {
                System.out.format("%12s  %d\n", key.toString(), ((LongWritable)value).get());
            } else if ( writable instanceof VectorWritable ) {
                System.out.format("%12s  %s\n", key.toString(), ((VectorWritable)value).get().asFormatString());                
            }        
        }
        reader.close();
    }
     
    public static void tokenizeDocuments(FileSystem fs, Configuration configuration, Path sequencePath, Path tokenizedPath)
            throws Exception {
 
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, configuration, sequencePath, Text.class, Text.class);
 
        // Source: http://en.wikipedia.org/wiki/Justice
        String documentId1 = "Concept of Justice";
        String text1 = "John Rawls claims that " +
                "\"Justice is the first virtue of social institutions, as truth is of systems of thought.\"[9] Justice can be thought of as distinct from benevolence, " +
                "charity, prudence, mercy, generosity, or compassion, although these dimensions are regularly understood to also be interlinked. " +
                "Studies at UCLA in 2008 have indicated that reactions to fairness are \"wired\" into the brain and that, " +
                "\"Fairness is activating the same part of the brain that responds to food in rats... This is consistent with the notion that being treated fairly satisfies a basic need\".[11] " +
                "Research conducted in 2003 at Emory University, Georgia, USA, involving Capuchin Monkeys demonstrated that other cooperative animals also possess such a sense and that " +
                "\"inequity aversion may not be uniquely human\"[12] indicating that ideas of fairness and justice may be instinctual in nature.";
        writer.append(new Text(documentId1), new Text(text1));
 
        String documentId2 = "Variations of Justice";
        String text2 = "Utilitarianism is a form of consequentialism, where punishment is forward-looking. " +
                "Justified by the ability to achieve future social benefits resulting in crime reduction, the moral worth of an action is determined by its outcome. " +
                "Retributive justice regulates proportionate response to crime proven by lawful evidence, " +
                "so that punishment is justly imposed and considered as morally correct and fully deserved. " +
                "The law of retaliation (lex talionis) is a military theory of retributive justice, " +
                "which says that reciprocity should be equal to the wrong suffered; \"life for life, wound for wound, stripe for stripe.\"[13] " +
                "Restorative justice is concerned not so much with retribution and punishment as with (a) making the victim whole and (b) reintegrating the offender into society. " +
                "This approach frequently brings an offender and a victim together, so that the offender can better understand the effect his/her offense had on the victim. " +
                "Distributive justice is directed at the proper allocation of things—wealth, power, reward, respect—among different people.";
        writer.append(new Text(documentId2), new Text(text2));
         
        // Source: http://en.wikipedia.org/wiki/Wisdom
        String documentId3 = "Wisdom";
        String text3 = "Wisdom is a deep understanding and realization of people, things, events or situations, resulting in the ability to apply perceptions, judgements and actions " +
                "in keeping with this understanding. It often requires control of one's emotional reactions (the \"passions\") so that universal principles, reason and knowledge " +
                "prevail to determine one's actions.";
        writer.append(new Text(documentId3), new Text(text3));
         
        writer.close();
 
        DocumentProcessor.tokenizeDocuments(sequencePath, StandardAnalyzer.class, tokenizedPath, configuration);
    }
     
    public static void processTfIdf(Configuration configuration, Path tokenizedPath) 
            throws Exception {
        boolean sequential    = false;
        boolean named         = false;
 
        Path wordCount = new Path( outputDir );
        Path tfidf = new Path( outputDir + "tfidf" );
 
        Path tfVectors = new Path( outputDir + DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER );
 
        // The tokenized documents must be in SequenceFile format
        DictionaryVectorizer.createTermFrequencyVectors(tokenizedPath,
                wordCount,
                DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER,
                configuration,
                2,                // the minimum frequency of the feature in the entire corpus to be considered for inclusion in the sparse vector
                1,                // maxNGramSize 1 = unigram, 2 = unigram and bigram, 3 = unigram, bigram and trigram
                0.0f,             // minValue of log likelihood ratio to used to prune ngrams
                PartialVectorMerger.NO_NORMALIZING,    // normPower L_p norm to be computed.
                false,            // whether to use log normalization
                1,                // numReducers
                100,              // chunkSizeInMegabytes
                sequential,        
                named);
 
        Pair<Long[], List<Path>> docFrequenciesFeatures = TFIDFConverter.calculateDF(tfVectors, 
                tfidf, configuration, 100);
 
        TFIDFConverter.processTfIdf(tfVectors,
                tfidf,
                configuration,
                docFrequenciesFeatures,
                1,                // The minimum document frequency. Default 1
                99,               // The max document frequency. Can be used to remove really high frequency features. 
                                  // Expressed as an integer between 0 and 100 (percentage). Default 99
                2.0f,
                true,             // whether to use log normalization
                sequential,
                named,
                1);               // The number of reducers to spawn
    }
}