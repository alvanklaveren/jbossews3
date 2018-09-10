package com.myforum.gameshop;

public enum ESortOrder {

	AZ		("A-Z"),
	ZA		("Z-A"),
	Rating	("Rating")
	;
	
	private String description;
	
	ESortOrder(String description){
		this.description 	= description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public static ESortOrder getSortOrder(String description) {
	    switch(description) {
	    case "A-Z": 	return ESortOrder.AZ;
	    case "Z-A": 	return ESortOrder.ZA;
	    case "Rating": 	return ESortOrder.Rating; 
	    default: 		return ESortOrder.AZ; 
	    }

	}

	public String toString(){
		return description;
	}

}
