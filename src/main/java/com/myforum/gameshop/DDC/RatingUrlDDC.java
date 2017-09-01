package com.myforum.gameshop.DDC;

import java.util.List;

import com.myforum.framework.AbstractDropDownChoice;
import com.myforum.tables.AVKTable;
import com.myforum.tables.ProductRating;
import com.myforum.tables.RatingUrl;
import com.myforum.tables.dao.RatingUrlDao;

public class RatingUrlDDC extends AbstractDropDownChoice{
	private static final long serialVersionUID = 1L;

	protected ProductRating productRating;
	
	public RatingUrlDDC(String wicketId, ProductRating table, String tableColumnName, boolean isAutoSave) {
		super(wicketId, table, tableColumnName, isAutoSave);
		productRating = (ProductRating) table;
	}

	protected List<RatingUrl> getList(){
		return (List<RatingUrl>) new RatingUrlDao().list();
	}	
	
	protected boolean update(AVKTable newSelection) {
		productRating.setRatingUrl((RatingUrl) newSelection);
		return true;
	}
}
