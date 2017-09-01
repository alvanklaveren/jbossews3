package com.myforum.base;

import java.util.Arrays;
import java.util.List;

import com.myforum.tables.Classification;
import com.myforum.tables.dao.ClassificationDao;

public class ForumLogics {
	
	public ForumLogics(){}
	
	public static List<Classification> getClassifications(Classification currentClassification, boolean shouldGetAll){
		List<Classification> classifications; // ArrayLists are serializable, Lists are not!

		// if shouldGetAll, then return all the classifications in the list
		if(shouldGetAll){
			// allow administrators to change the classification of a user
			classifications = (List<Classification>) new ClassificationDao().list();		
		}else{
			// only add the current forum user's classification to the list
			classifications = (List<Classification>) Arrays.asList( currentClassification );
		}
		return classifications;
	}	
}
