package com.myforum.base.dictionary;

public final class DictionaryEnglish extends Dictionary{
	
	DictionaryEnglish(ELanguage language){
		super(language);

		translatedWordMap.put("accessoires", "accessories"); // accidently used the dutch/french variant. Translate to english !!

		StringBuilder aboutText = new StringBuilder();
		aboutText.append( "Alvanklaveren.com is created, written and maintained by A.L. van Klaveren. It is (currently) built " );  
		aboutText.append( "in Java (EE), using <a href=\"http://hibernate.org/\">Apache Hibernate</a>, <a href=\"http://wicket.apache.org/\">Apache Wicket</a> " );
		aboutText.append( "and <a href=\"https://spring.io/\">Spring Framework</a>. The website is running on <a href=\"http://tomcat.apache.org/\">Tomcat 8</a> " );
		aboutText.append( "on a hosting platform called <a href = \"https://www.digitalocean.com/\">Digital Ocean</a>. Last but not least, the website's layout is " );
		aboutText.append( "supported by <a href=\"http://getbootstrap.com/\"> Bootstrap</a>. <br><br> " );
		aboutText.append( "If you have a question or just want to contact the author, please leave a " );
		put(translatedSentenceMap, EText.ABOUT_WEBSITE_TEXT, aboutText.toString());
	}
	
}
