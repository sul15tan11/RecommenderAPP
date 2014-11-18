 package com.predection.recommender;

import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

import java.io.*;
import java.util.*;

public class MovieRecommender {

	private MovieRecommender() {
	  }

	  public static void main(String[] args) throws Exception {

	    DataModel model = new FileDataModel(new File("data/movies.csv"));
	    //C:\Users\psxsa16\workspace\com.rukbysoft.examples.mahout-MahoutRecommender\com.rukbysoft.examples.mahout-MahoutRecommender\src\main\java\com\rukbysoft\examples\mahout\recommender
	    //DataModel model = new FileDataModel(new File("/Users/psxsa16/workspace/com.rukbysoft.examples.mahout/src/main/java/com/rukbysoft/examples/mahout/recommender/MoviesList.csv"));

	    /*Here we are using Pearson's Correlation
	     * it has limitations such as that it does not account
	     * for the NUMBER of items in which 2 users' preferences overlap.
	     * There are other options such as Euclidean Distance similarity.
	    */
	    
	    UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	    
	    //First parameter - neighborhood size; capped at the number of users in the data model
	    UserNeighborhood neighborhood =
	      new NearestNUserNeighborhood(7, similarity, model);

	    //UserBasedRecommender recommender = new GenericUserBasedRecommender(
        // model, neighborhood, similarity)
	    
	    Recommender recommender = new GenericUserBasedRecommender(
	        model, neighborhood, similarity);

	    //First parameter is UserID, the second is the number of items to be recommended
	    List<RecommendedItem> recommendations =
	        recommender.recommend(163,10);

	    for (RecommendedItem recommendation : recommendations) {
	      System.out.println(recommendation);
	    }
	    
	    
	    // Using cachingRecommender with "Recommender" 
	    
	    
	   /* 
	    Recommender cachingRecommender = new CachingRecommender(recommender);
	    List<RecommendedItem> recommendations2 =
	    		  cachingRecommender.recommend(1, 3);
	   
	    System.out.println("");
	    
	    for (RecommendedItem recommendation : recommendations2) {
		      System.out.println(recommendation);
		    }
		    
	     */
	  }

/*  public static void main(String[] args) {
			com.rukbysoft.examples.mahout.recommender;
		} */
}
