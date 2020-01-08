package com.myforum.dictionary;

public final class DictionaryEnglish extends Dictionary{
	
	DictionaryEnglish(ELanguage language){
		super(language);

		translatedWordMap.put("accessoires", "accessories"); // accidently used the dutch/french variant. Translate to english !!

		StringBuilder aboutText = new StringBuilder();
		aboutText.append( "This website is created, written and maintained by A.L. van Klaveren.<br>It is (currently) built " );  
		aboutText.append( "in Java (EE), using <a href=\"http://hibernate.org/\">Apache Hibernate</a>, <a href=\"http://wicket.apache.org/\">Apache Wicket</a> " );
		aboutText.append( "and <a href=\"https://spring.io/\">Spring Framework</a>.<br>The website is running on <a href=\"http://tomcat.apache.org/\">Tomcat 8</a> " );
		aboutText.append( "on a hosting platform called <a href=\"https://www.digitalocean.com/\">Digital Ocean</a>.<br>For the layout it uses " );
		aboutText.append( "<a href=\"http://getbootstrap.com/\"><i class=\"fab fa-bootstrap\"></i> Bootstrap</a> and <a href=\"https://fontawesome.com/\"><i class=\"fab fa-fort-awesome-alt\"></i> Font Awesome</a>. <br><br> " );
		aboutText.append( "If you have a question or just want to contact the author, please leave a " );
		put(translatedSentenceMap, EText.ABOUT_WEBSITE_TEXT, aboutText.toString());
	}
	
}
