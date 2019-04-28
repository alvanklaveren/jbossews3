package com.myforum.dictionary;

public enum ELanguage {

  English (1, "English")
, Dutch   (2, "Dutch"),    
;

	String  description;
	int		id; 
	
	ELanguage( int id, String description ){
		this.id = id;
		this.description = description;
	}
	
	Dictionary getDictionary(){
		switch(this){
		case English:	return new DictionaryEnglish(this);
			
		case Dutch:		return new DictionaryDutch(this);
			
		default:		return new DictionaryEnglish(this);
		}
	}

	public int getId(){
		return id;
	}
	
	public static ELanguage getLanguage(int id){
		switch(id){
		case 1:		return ELanguage.English;
		case 2:		return ELanguage.Dutch;
		default:	return ELanguage.English;
		}
	}
	
	public String toString(){
		return description;
	}
}
