package com.predictionmarkiting.RecommenderApp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Hello world!
 *
 * * Different measures of similarities :
			 * 1. LogLikelihoodSimilarity
			 * 2. TanimotoCoefficientSimilarity
			 * 3. TanimotoCoefficientSimilarity
			 * 4. SpearmanCorrelationSimilarity
			 * 5. PearsonCorrelationSimilarity
			 * 
 */
public class RecommenderDiffrentAlgro 
{
    public static void main( String[] args ) throws Exception
    {
    	
    	DataModel model = new FileDataModel(new File("data/movies.csv"));
    	
    	UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
    	
    	UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, similarity, model);
    	UserBasedRecommender recommender = 
    			new GenericUserBasedRecommender(model, neighborhood, similarity);
    	
    	
	    //First parameter is UserID, the second is the number of items to be recommended
    	List <RecommendedItem> recommendations = recommender.recommend(303, 5);
    	
	      System.out.println(" \n-------------------------ThresholdUserNeighborhood(0.7)-------------------------------");

    	for (RecommendedItem recommendation : recommendations) {
    	  System.out.println(recommendation);
    	}

	      System.out.println(" \n\n-------------------------NearestNUserNeighborhood(7)----------------------------------");

    		    
	    //First parameter - neighborhood size; capped at the number of users in the data model
	     neighborhood =
	      new NearestNUserNeighborhood(7, similarity, model);

	    //UserBasedRecommender recommender = new GenericUserBasedRecommender(
        // model, neighborhood, similarity)
	    
	     recommender = new GenericUserBasedRecommender(
	        model, neighborhood, similarity);

	    //First parameter is UserID, the second is the number of items to be recommended
	    recommendations =
	        recommender.recommend(303,5);

	    for (RecommendedItem recommendation : recommendations) {
	      System.out.println(recommendation);
	    }
    	
	      System.out.println("----------------------------------------------------------------------------------------\n\n");

    	
    	//----------------- NEw ADDED TO SEE DIFFRENT TYPES OF RECOMMENDATION ALGORITHIMS ---------
    	
    	
    	/*
		 * 
		 * Different measures of similarities :
		 * 1. LogLikelihoodSimilarity
		 * 2. TanimotoCoefficientSimilarity
		 * 3. TanimotoCoefficientSimilarity
		 * 4. SpearmanCorrelationSimilarity
		 * 5. PearsonCorrelationSimilarity
		 * 
		 */
	
	  
    	try {
			DataModel dm = new FileDataModel(new File("data/movies.csv"));

			
		

			// 1. LogLikelihoodSimilarity
			//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
			
			// 2. TanimotoCoefficientSimilarity
		TanimotoCoefficientSimilarity sim = new TanimotoCoefficientSimilarity(dm);
			
			// 3. PearsonCorrelationSimilarity
			//PearsonCorrelationSimilarity sim = new PearsonCorrelationSimilarity(dm);
			
			GenericItemBasedRecommender recommender2 = new GenericItemBasedRecommender(dm, sim);

			int x=1;
			for(LongPrimitiveIterator items = dm.getItemIDs(); items.hasNext();) {
				long itemId = items.nextLong();
				List<RecommendedItem>recommendations2 = recommender2.mostSimilarItems(itemId, 10);

				// if we want to see only items taht are similar to item 14 with a value
				if(itemId==112 )
					
				for(RecommendedItem recommendation : recommendations2) {
					// recommendation.getValue(): value from 0-1 of how similar the two items are  
					System.out.println(itemId + "," + recommendation.getItemID() + "," + recommendation.getValue());
				}
				
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
    	
    

    
	//-----------------------------------------------------------------------------------------------------------

    }
    
}
