package com.myforum.base.dictionary;

public final class DictionaryEnglish extends Dictionary{
	
	DictionaryEnglish(ELanguage language){
		super(language);

		translatedWordMap.put("accessoires", "accessories"); // accidently used the dutch/french variant. Translate to english !!
	}
	
}
