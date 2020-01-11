package com.myforum.dictionary;

import java.util.HashMap;
import java.util.Map;

import com.myforum.tables.dao.TranslationDao;


public final class Dictionary{
	
	private ELanguage eLanguage;
	
	protected Map<String, String> translatedWordMap     = new HashMap<String, String>();
	protected Map<String, String> translatedSentenceMap = new HashMap<String, String>();
	
	protected void put( Map<String,String> map, EText eText, String sentence){
		map.put(eText.toString(), sentence);
	}; 
	
	Dictionary(ELanguage eLanguage){
		this.eLanguage = eLanguage;
	}

	public ELanguage getLanguage(){
		return eLanguage;
	}

	public String toString(){
		return getLanguage().toString();
	}

	public String translate(String text){
		
		if(text.isEmpty() ) { return text; }

		// first check if the word or sentence is IN the database table TRANSLATION
		String foreignText = new TranslationDao().findByLanguage(eLanguage.getLanguageId(), text);
		if(foreignText != null) {
			// if the word started with upper case, make sure the foreign words ALSO starts with upper case
			return text.charAt(0) != text.toLowerCase().charAt(0) ? camelCase(foreignText): foreignText;
		}

		// if it did not find the original text, then look at the words in variable text one by one 
		String[] splittedSentence = text.split( "\\s+");
		
		// only one string (or less) in the array? Then just return text.
		if (splittedSentence.length <= 1) return text; 
		
		StringBuilder foreignSB = new StringBuilder();
		
		for(int pos=0; pos < splittedSentence.length; pos++) {
			foreignSB.append(translate(splittedSentence[pos]));
			if(pos<splittedSentence.length-1) foreignSB.append(" ");
		}
		return foreignSB.toString();
	};
	
	private String camelCase(String text) {
		return text.toUpperCase().charAt(0) + text.substring(1);
	}
	
}
