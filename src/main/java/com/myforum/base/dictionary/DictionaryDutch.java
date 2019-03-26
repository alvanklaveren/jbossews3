package com.myforum.base.dictionary;

public final class DictionaryDutch extends Dictionary {

	DictionaryDutch(ELanguage language){
		super(language);
		translatedWordMap.put("about", 			"over");
		translatedWordMap.put("and", 			"en");
		translatedWordMap.put("already", 		"reeds");
		translatedWordMap.put("applications", 	"applicaties");
		translatedWordMap.put("articles", 		"artikelen");
		translatedWordMap.put("cancel", 		"annuleer");
		translatedWordMap.put("category", 		"categorie");
		translatedWordMap.put("categories", 	"categorieen");
		translatedWordMap.put("collection", 	"verzameling");
		translatedWordMap.put("delete", 		"verwijder");
		translatedWordMap.put("description", 	"omschrijving");
		translatedWordMap.put("empty", 			"leeg");
		translatedWordMap.put("exists", 		"bestaat");
		translatedWordMap.put("experience", 	"ervaring");
		translatedWordMap.put("firsthand", 		"eerste");
		translatedWordMap.put("frameworks", 	"frameworks");
		translatedWordMap.put("from", 			"van");
		translatedWordMap.put("galaxy", 		"sterrenstelsel");
		translatedWordMap.put("game", 			"spellen");
		translatedWordMap.put("hello", 			"hallo");
		translatedWordMap.put("home", 			"hoofdpagina");
		translatedWordMap.put("ide", 			"ontwikkelomgeving");
		translatedWordMap.put("ides", 			"ontwikkelomgevingen");
		translatedWordMap.put("languages", 		"talen");
		translatedWordMap.put("login", 			"aanmelden");
		translatedWordMap.put("logout",			"afmelden");
		translatedWordMap.put("me", 			"mij");
		translatedWordMap.put("message", 		"bericht");
		translatedWordMap.put("modify", 		"wijzig");
		translatedWordMap.put("my", 			"mijn");
		translatedWordMap.put("no", 			"nee");
		translatedWordMap.put("of", 			"van");
		translatedWordMap.put("password", 		"wachtwoord");
		translatedWordMap.put("preview",		"voorvertoning");
		translatedWordMap.put("product",		"produkt");
		translatedWordMap.put("rating", 		"beoordeling");
		translatedWordMap.put("refresh", 		"ververs");
		translatedWordMap.put("register", 		"registreer");
		translatedWordMap.put("rename", 		"hernoem");
		translatedWordMap.put("save", 			"bewaar");
		translatedWordMap.put("search", 		"zoek");
		translatedWordMap.put("send", 			"verstuur");
		translatedWordMap.put("the", 			"de");
		translatedWordMap.put("title", 			"titel");
		translatedWordMap.put("to", 			"tot");
		translatedWordMap.put("username", 		"gebruikersnaam");
		translatedWordMap.put("welcome", 		"welkom");
		translatedWordMap.put("with", 			"met");
		translatedWordMap.put("yes", 			"ja");
		translatedWordMap.put("message", 		"bericht");
		
		put(translatedSentenceMap, EText.ADD_CATEGORY, "Category toevoegen");
		put(translatedSentenceMap, EText.ADD_PRODUCT, "Product toevoegen");
		put(translatedSentenceMap, EText.ADD_COMPANY, "Bedrijf toevoegen");
		put(translatedSentenceMap, EText.ADD_RATING_URL, "Beoordeling URL toevoegen");
		put(translatedSentenceMap, EText.ADMINISTRATOR_MAINTENANCE, "Administrator Onderhoud");
		put(translatedSentenceMap, EText.APPLY_CHANGES, "Wijzigingen Toepassen");
		put(translatedSentenceMap, EText.CIB_LABEL, "CIB = compleet in box, maar moet hier worden gelezen als \"bevat in ieder geval een handleiding\"" );
		put(translatedSentenceMap, EText.DO_DELETE_CATEGORY, "Wil je deze category echt verwijderen?");
		put(translatedSentenceMap, EText.FAILED_GENERATE_PASSWORD, "Aanmaken nieuw wachtwoord is mislukt." );
		put(translatedSentenceMap, EText.FORGOT_PASSWORD, "Wachtwoord vergeten?");
		put(translatedSentenceMap, EText.GAME_CONSOLE, "Spel Computer");
		put(translatedSentenceMap, EText.INCORRECT_LOGIN_CREDENTIALS, "Onjuiste naam en wachtwoord");
		put(translatedSentenceMap, EText.MOVE_MESSAGE, "Verplaats Bericht Naar Andere Category");
		put(translatedSentenceMap, EText.NUMBER_OF_ITEMS, "Aantal per Pagina");
		put(translatedSentenceMap, EText.PRODUCT_TYPE, "Type Product");
		put(translatedSentenceMap, EText.PLEASE_ADD_URL, "Voeg een URL toe" );
		put(translatedSentenceMap, EText.PLEASE_APPLY_RATING, "Voeg een waarde van waardering toe" );
		put(translatedSentenceMap, EText.RECENTLY_ADDED, "Recent toegevoegde producten");
		put(translatedSentenceMap, EText.REGISTER_NEW_USER, "Registreer Nieuwe Gebruiker");
		put(translatedSentenceMap, EText.SEARCH_RESULTS, "Zoek resultaten");
		put(translatedSentenceMap, EText.UPLOAD_FAILED, "Uploaden van bestand mislukt. Controleer de naam en de grootte (<100kb) van het bestand.");
		put(translatedSentenceMap, EText.UPDATE_PROFILE, "Pas Profiel Aan");
		put(translatedSentenceMap, EText.ABOUT_WEBSITE, "Over ...");

		StringBuilder aboutText = new StringBuilder();
		aboutText.append( "Deze website is gemaakt, geschreven en wordt onderhouden door  A.L. van Klaveren. Momenteel is het gebouwd in " ); 
		aboutText.append( "Java (EE), gebruikmakend van <a href=\"http://hibernate.org/\">Apache Hibernate</a>, <a href=\"http://wicket.apache.org/\">Apache Wicket</a>, " );
		aboutText.append( "en <a href=\"https://spring.io/\">Spring Framework</a>. De website draait op <a href=\"http://tomcat.apache.org/\">Tomcat 8</a> " );
		aboutText.append( "op een hosting platform genaamd <a href = \"https://www.digitalocean.com/\">Digital Ocean</a>. Verder wordt de layout van de website " ); 
		aboutText.append( "ondersteunt door <a href=\"http://getbootstrap.com/\"> Bootstrap</a>. <br><br>" ); 
		aboutText.append( "Als je een vraag hebt of om een andere reden contact wilt zoeken met de maker van deze website, stuur dan een " ); 
		put(translatedSentenceMap, EText.ABOUT_WEBSITE_TEXT, aboutText.toString());
		
	}
	
}
