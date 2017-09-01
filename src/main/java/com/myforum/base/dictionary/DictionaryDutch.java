package com.myforum.base.dictionary;

public final class DictionaryDutch extends Dictionary {

	DictionaryDutch(ELanguage language){
		super(language);
		translatedWordMap.put("to", 		"tot");
		translatedWordMap.put("of", 		"van");
		translatedWordMap.put("from", 		"van");
		translatedWordMap.put("Search", 	"Zoek");
		translatedWordMap.put("About", 		"Over");
		translatedWordMap.put("Hello", 		"Hallo");
		translatedWordMap.put("Galaxy", 	"Sterrenstelsel");
		translatedWordMap.put("Articles", 	"Artikelen");
		translatedWordMap.put("Firsthand", 	"Eerste");
		translatedWordMap.put("experience", "ervaring");
		translatedWordMap.put("with", 		"met");
		translatedWordMap.put("Languages", 	"Talen");
		translatedWordMap.put("Frameworks", "Frameworks");
		translatedWordMap.put("and", 		"en");
		translatedWordMap.put("IDE", 		"Ontwikkelomgeving");
		translatedWordMap.put("IDEs", 		"Ontwikkelomgevingen");
		translatedWordMap.put("Rating", 	"Beoordeling");

		translatedWordMap.put("Game Console", 		"Spel Computer");
		translatedWordMap.put("Product Type", 		"Product Type");
		translatedWordMap.put("Number of Items", 	"Aantal per Pagina");
		
		put(translatedSentenceMap, EText.RECENTLY_ADDED, "Recent toegevoegde producten");
		put(translatedSentenceMap, EText.UPLOAD_FAILED, "Uploaden van bestand mislukt. Controleer de naam en de grootte (<100kb) van het bestand.");
		put(translatedSentenceMap, EText.ADD_PRODUCT, "Product toevoegen");
		put(translatedSentenceMap, EText.ADD_COMPANY, "Bedrijf toevoegen");
		put(translatedSentenceMap, EText.ADD_RATING_URL, "Beoordeling URL toevoegen");
		put(translatedSentenceMap, EText.REFRESH, "Ververs");
		put(translatedSentenceMap, EText.SEARCH_RESULTS, "Zoek resultaten");
	}
	
}
