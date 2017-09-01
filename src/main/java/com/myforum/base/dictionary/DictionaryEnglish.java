package com.myforum.base.dictionary;

public final class DictionaryEnglish extends Dictionary{
	
	DictionaryEnglish(ELanguage language){
		super(language);
		translatedWordMap.put("to", "to");
		translatedWordMap.put("of", "of");
		translatedWordMap.put("from", "from");
		translatedWordMap.put("Search", "Search");
		translatedWordMap.put("About", "About");
		translatedWordMap.put("Hello", "Hello");
		translatedWordMap.put("Galaxy", "Galaxy");
		translatedWordMap.put("Articles", "Articles");
		translatedWordMap.put("Firsthand", "Firsthand");
		translatedWordMap.put("experience", "experience");
		translatedWordMap.put("with", "with");
		translatedWordMap.put("Languages", "Languages");
		translatedWordMap.put("Frameworks", "Frameworks");
		translatedWordMap.put("and", "and");
		translatedWordMap.put("IDE", "IDE");
		translatedWordMap.put("IDEs", "IDEs");
		translatedWordMap.put("Rating", "Rating");
			
		translatedWordMap.put("Game Console", "Game Console");
		translatedWordMap.put("Product Type", "Product Type");
		translatedWordMap.put("Number of Items", "Number of Items");

		put(translatedSentenceMap, EText.RECENTLY_ADDED, 	EText.RECENTLY_ADDED.toString());
		put(translatedSentenceMap, EText.UPLOAD_FAILED,		EText.UPLOAD_FAILED.toString());
		put(translatedSentenceMap, EText.ADD_PRODUCT, 		EText.ADD_PRODUCT.toString());
		put(translatedSentenceMap, EText.ADD_COMPANY, 		EText.ADD_COMPANY.toString());
		put(translatedSentenceMap, EText.REFRESH, 			EText.REFRESH.toString());
		put(translatedSentenceMap, EText.SEARCH_RESULTS, 	EText.SEARCH_RESULTS.toString());
	}
	
}
