package com.myforum.dictionary;

public enum ELanguage {

  English (1, "English", "us", "/fonts/flag_gb.svg", "UK", 30, 20)
, Dutch   (2, "Dutch", "nl", "/fonts/flag_nl.svg", "NL", 20, 20),    
;

	String  description;
	int		id;
	String  languageId;
	String	fontFile;
	String 	isoA2;
	int 	fontWidth;
	int 	fontHeight;
	
	Dictionary dictionary;
	
	ELanguage(int id, String description, String languageId, String fontfile, String isoA2, int fontWidth, int fontHeight){
		this.id = id;
		this.description = description;
		this.languageId = languageId;
		this.fontFile = fontfile;
		this.isoA2 = isoA2;
		this.fontWidth = fontWidth;
		this.fontHeight = fontHeight;
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
	
	public String getFontFile() {		
		return fontFile;
	}

	public String getIsoA2() {		
		return isoA2;
	}

	public String toString(){
		return description;
	}
	
	public int getFontWidth() {
		return fontWidth;
	}
	
	public int getFontHeight() {
		return fontHeight;
	}
	
}
