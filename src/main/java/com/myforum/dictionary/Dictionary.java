package com.myforum.dictionary;

import java.util.HashMap;
import java.util.Map;


/*
 * Abstract Dictionary, containing the default mapping on English
 */
public abstract class Dictionary{
	private ELanguage language;
	
	protected Map<String, String> translatedWordMap     = new HashMap<String, String>();
	protected Map<String, String> translatedSentenceMap = new HashMap<String, String>();
	
	protected void put( Map<String,String> map, EText eText, String sentence){
		map.put(eText.toString(), sentence);
	}; 
	
	Dictionary(ELanguage language){
		this.language = language;
	}

	public ELanguage getLanguage(){
		return language;
	}

	public String toString(){
		return getLanguage().toString();
	}

	public String translate(String text){
		if(text == null) 	return null;
		if(text.equals("")) return "";

		String foreignText = null;
		// first check whether it is a full sentence
		foreignText = translatedSentenceMap.get(text);
		
		if( foreignText != null){ 
			return foreignText; 
		}

		// if it did not translate, then maybe it is just one word
		
		// when translating, ignore uppercase
		String lowercaseText = text.toLowerCase();
		foreignText = translatedWordMap.get(lowercaseText);
		
		// when the first char is uppercase, then force the first char to be uppercase in the foreignt text as well
		if( text.charAt(0) != lowercaseText.charAt(0) && foreignText != null ){
			foreignText = foreignText.toUpperCase().charAt(0) + foreignText.substring(1);
		}

		if( foreignText != null ){ 
			return foreignText; 
		}

		// It could still be a full sentence, however it appears to NOT have been specifically declared in the dictionary.
		// Translate as many words as possible.
		String[] splittedSentence = text.split( "\\s+");
		
		// when there is only one string (or less) in the array, then just return text... no translation appeared to be possible...
		if (splittedSentence.length <= 1) return text; 
		
		StringBuilder foreignSB = new StringBuilder();
		
		for(int pos=0; pos < splittedSentence.length; pos++) {
			foreignSB.append(translate(splittedSentence[pos]));
			if(pos<splittedSentence.length-1) foreignSB.append(" ");
		}
		return foreignSB.toString();
	};
	
}
