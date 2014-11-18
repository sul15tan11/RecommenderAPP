package com.predictionmarkiting.RecommenderApp;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class EvaluateRecommender {

	/**
	 * @param args
	 * @throws Exception 
	 * 
	 * evaluate in interface RecommenderEvaluator
	  
	  double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
	  
	   Parameters: 
	- recommenderBuilder : object that can build a Recommender to test
	- dataModelBuilder : DataModelBuilder to use, or if null, a default DataModel implementation will be used
	- dataModel : dataset to test on
	- trainingPercentage : percentage of each user's preferences to use to produce recommendations;
	 					   the rest are compared to estimated preference values to evaluate Recommender performance
	- evaluationPercentage : percentage of users to use in evaluation
	   
	   Returns:
	   "score" representing how well the Recommender's estimated preferences match real values; lower 
	         scores mean a better match and 0 is a perfect match
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		// from the 10% that we tested & evaluated; the percentage shows the average error of guessing is :.... 
		DataModel model = new FileDataModel(new File("data/movies.csv"));
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		// from the 10% that we tested we evaluated the average error of guessing is : .... 
		System.out.println(result);
		
		// Note: if you run this test multiple times, you will get different results,
		//because the splitting into trainingset and testset is done randomly.
		
	}

}


class MyRecommenderBuilder  implements RecommenderBuilder {
	
	public Recommender buildRecommender (DataModel dataModel) throws TasteException{
			//UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
			//ItemSimilarity similarity = new SpearmanCorrelationSimilarity(dataModel);
			//UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
		//ItemSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
			
			//UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, similarity, dataModel);
			//UserNeighborhood neighborhood = new NearestNUserNeighborhood(7, similarity, dataModel);
		
			//return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
	
	ItemSimilarity similarity = new LogLikelihoodSimilarity(dataModel);

return new GenericItemBasedRecommender(dataModel, similarity);
	
	}
}