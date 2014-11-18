package com.tfifd.calcualation;

	
	import java.util.HashMap;
	import java.util.Iterator;
	import java.util.Map;

	
	
	import org.apache.mahout.vectorizer.tfidf.*;

	
	public class TestTF_IDF {
		
		public static void main(String[] args){
			//Test code for TfIdf
			TfIdf tf = new TfIdf("data/movies.csv");
			//Contains words in the documents
			String word;
			//Contains file name being processed
			String file;
			//Variable to hold document frequency and IDF of each word
			Double[] dfIDF;
			
			//Print document frequency and IDF of every word in the corpus
			for (Iterator<String> it = tf.allwords.keySet().iterator(); it.hasNext(); ) {
				word = it.next();
				dfIDF = tf.allwords.get(word);
				//dfIDF[0] is the DF of the word and dfIDF[1] is the IDF of the word
				System.out.println("Term " + word + " " + " Document Frequency " + dfIDF[0] + " " + " IDF " + dfIDF[1]);
			}//for (Iterator<String> it = tf.allwords.keySet().iterator(); it.hasNext(); )	
			
			tf.buildAllDocuments();
			
			//Print TF-IDF of each document in the corpus
			for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); ) {
				file = it.next();
				System.err.println("File Name " + file + "\t" + "TF-IDF " + tf.documents.get(file).vectorlength);
			}//for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); )
			
			//Prints each term in a document, its frequency, term frequency and tf-idf
			Map<String, Double[]> myMap = new HashMap<String, Double[]>();
			Double[] values;
			for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); ) {
				file = it.next();
				System.out.println("File \t" + file);
				
				myMap = tf.documents.get(file).getF_TF_TFIDF();
				
				for (String key : myMap.keySet()) {
					
					values = myMap.get(key);
				    System.out.println("Term = " + key + " Frequency = " + values[0] + " Term Frequency " + values[1] + " TF-IDF " + values[2]);
				  
				}//for (String key : myMap.keySet())
			}//for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); )
			
		}//public static void main(String[] args)
	}//public class TestTF_IDF