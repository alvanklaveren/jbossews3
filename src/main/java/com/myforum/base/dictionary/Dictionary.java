package com.myforum.base.dictionary;

import java.util.HashMap;
import java.util.Map;

/*
 * Abstract Dictionary, containing the default mapping on English
 */
public abstract class Dictionary{
	private ELanguage language;
	
	protected Map<String, String> translatedWordMap    = new HashMap<String, String>();
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
		String foreignText = null;
		// first check whether it is a full sentence
		foreignText = translatedSentenceMap.get(text);
		
		if( foreignText != null){ 
			return foreignText; 
		}

		// if it did not translate, then maybe it is just one word
		foreignText = translatedWordMap.get(text);

		if( foreignText != null){ 
			return foreignText; 
		}
		
		return text;
	};
	
}
