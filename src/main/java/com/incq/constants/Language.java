package com.incq.constants;

public class Language {
	public static final String[] langString =  {"English"};
	public static final int DEFAULTLNAG = 0;

	public static int getLanguageNum(String lang) {
		int theReturn = DEFAULTLNAG;
		for(int x=0; x < langString.length; x++) {
			if(langString[x].equals(lang)) {
				theReturn = x;
				break;
			}
		}
		return theReturn;
	}
	public static String getLanguage(int lang) {
		return langString[lang];
	}
}
