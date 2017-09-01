package com.myforum.base.dictionary;

import java.io.Serializable;

import com.myforum.application.CookieLogics;

/*
 * Translator, to display the website in different languages
 */

public final class Translator implements Serializable{
	private static final long serialVersionUID = 1L;

	static Translator instance;
	
	static{
		instance = new Translator();
	}
	
	private Translator(){
		// singleton
	}
	
	public static Translator getInstance(){
		return instance;
	}
	
	/*
	 * Compares the text in the argument to the Enum table for a particular language
	 * In a later phase, this could later be stored to the database
	 */
	public String translate(String text, ELanguage language){
		return language.getDictionary().translate(text);
	};

	public String translate(String text){
		return translate(text, getDefaultLanguage());
	};

	public String translate(EText eText){
		return translate(eText.toString(), getDefaultLanguage());
	};

	public String translateFullSentence(String sentence){
		return translateFullSentence(sentence, getDefaultLanguage());
	}
	
	public String translateFullSentence(String sentence, ELanguage language){
		String translatedSentence = translate(sentence.trim(), language);

		// when no translation occurred, try to split the string up
		if(translatedSentence.equals(sentence.trim())){
			StringBuilder sb = new StringBuilder();
			String[] words = sentence.trim().split("\\s+");
			for(String word:words){
				sb.append( translate(word) );
				sb.append(" ");
			}
			
			translatedSentence = sb.toString().trim();
		}
		
		return translatedSentence;
	}
	
	/*
	 * Retrieves the default language, set for the specific browserclient
	 */
	public ELanguage getDefaultLanguage(){
		int defaultLanguage = CookieLogics.getCookieInt("defaultLanguage");
		if( defaultLanguage == 0 ){
			defaultLanguage = ELanguage.English.getId();
			CookieLogics.setCookieForever("defaultLanguage", defaultLanguage);
		}
		
		return ELanguage.getLanguage(defaultLanguage);
	}
	
	public void setDefaultLanguage(ELanguage language){
		CookieLogics.setCookieForever("defaultLanguage", language.getId());
	}

}
