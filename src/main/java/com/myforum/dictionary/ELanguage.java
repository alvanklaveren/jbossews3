package com.myforum.dictionary;

public enum ELanguage {

  English (1, "English", "us")
, Dutch   (2, "Dutch", "nl"),    
;

	String  description;
	int		id;
	String  languageId;
	Dictionary dictionary;
	
	ELanguage(int id, String description, String languageId){
		this.id = id;
		this.description = description;
		this.languageId = languageId;
		this.dictionary = new Dictionary(this);
	}
	
	Dictionary getDictionary(){
		return dictionary;
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
	
	public String getLanguageId() {
		return languageId;
	}
	
	public String toString(){
		return description;
	}
}
