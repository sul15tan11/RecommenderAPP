package com.predictionmarketing.itemrecommend;

	import java.io.File;
	import java.io.IOException;
	import java.util.List;

	import org.apache.mahout.cf.taste.common.TasteException;
	import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
	import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
	import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
	import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
	import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
	import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
	import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
	import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
	import org.apache.mahout.cf.taste.model.DataModel;
	import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
	import org.apache.mahout.cf.taste.recommender.RecommendedItem;
	import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
	import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

	public class ItemsPredectionForUserPe {

		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub

			try {
				DataModel dm = new FileDataModel(new File("data/movies.csv"));

				
				/*
				 * 
				 * Different measures of similarities :
				 * 1. LogLikelihoodSimilarity
				 * 2. TanimotoCoefficientSimilarity
				 */

				// 1. LogLikelihoodSimilarity
				//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
				
				// 2. TanimotoCoefficientSimilarity
				//TanimotoCoefficientSimilarity sim = new TanimotoCoefficientSimilarity(dm);
				
				// 3. PearsonCorrelationSimilarity
				PearsonCorrelationSimilarity sim = new PearsonCorrelationSimilarity(dm);
				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, sim, dm);
		    	UserBasedRecommender recommender = 
		    			new GenericUserBasedRecommender(dm, neighborhood, sim);
		    	
				//GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);
		        //First parameter is UserID, the second is the number of items to be recommended
			    List<RecommendedItem> recommendations =
			        recommender.recommend(100,10);

			    
				int x=1;
			    for (RecommendedItem recommendation : recommendations) {
			      System.out.println(recommendation);

					x++;
					// to show only the first 10 items
					//if(x>10) System.exit(1); 
				}



			} catch (IOException e) {
				System.out.println("There was an error.");
				e.printStackTrace();
			} catch (TasteException e) {
				System.out.println("There was a Taste Exception");
				e.printStackTrace();
			}

		}

	}

