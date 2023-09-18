package com.incq.constants;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    ENGLISH("en", "English", "&#127482;&#127480;"),
    ARABIC("ar", "Arabic", "&#127462;&#127487;"),
    MANDARIN_CHINESE("zh", "Mandarin Chinese", "&#127464;&#127475;"),
    DANISH("da", "Danish", "&#127465;&#127472;"),
    DUTCH("nl", "Dutch", "&#127475;&#127473;"),
    FRENCH("fr", "French", "&#127467;&#127479;"),
    GERMAN("de", "German", "&#127465;&#127466;"),
    HINDI("hi", "Hindi", "&#127470;&#127475;"),
    ITALIAN("it", "Italian", "&#127470;&#127481;"),
    JAPANESE("ja", "Japanese", "&#127471;&#127477;"),
    KOREAN("ko", "Korean", "&#127472;&#127479;"),
    NORWEGIAN("no", "Norwegian", "&#127475;&#127476;"),
    PORTUGUESE("pt", "Portuguese", "&#127477;&#127479;"),
    RUSSIAN("ru", "Russian", "&#127479;&#127482;"),
    SPANISH_SPAIN("es-ES", "Spanish (Spain)", "&#127466;&#127480;"),
    SPANISH_MEXICO("es-MX", "Spanish (Mexico)", "&#127474;&#127485;"),   
    SPANISH_COLOMBIA("es-CO", "Spanish (Colombia)", "&#127464;&#127476;"),      
    SPANISH_ARGENTINA("es-AR", "Spanish (Argentina)", "&#127462;&#127479;"),
    SPANISH_VENEZUELA("es-VE", "Spanish (Venezuela)", "&#127483;&#127466;"),
    SPANISH_PERU("es-PE", "Spanish (Peru)", "&#127477;&#127486;"),
    SWEDISH("sv", "Swedish", "&#127480;&#127465;"),
    SWISS_GERMAN_FRENCH("ch", "Swiss German/Swiss French", "&#127464;&#127469;"),
    TURKISH("tr", "Turkish", "&#127481;&#127479;"),
    HEBREW("he", "Hebrew", "&#127473;&#127482;"),
    SINGLISH_MALAY("sg", "Singaporean English (Singlish)/Malay", "&#127464;&#127472;");

    private static final Map<String, Language> BY_CODE = new HashMap<>();
    private static final Map<String, Language> BY_NAME = new HashMap<>();
    static {
        for (Language lang : values()) {
            BY_CODE.put(lang.code, lang);
            BY_NAME.put(lang.name, lang);
        }
    }

    public final String code;
    public final String name;
    public final String flagUnicode;

    Language(String code, String name, String flagUnicode) {
        this.code = code;
        this.name = name;
        this.flagUnicode = flagUnicode;
    }

    public static Language findByCode(String code) {
    	Language lang = BY_CODE.getOrDefault(code, ENGLISH);
    	if(ENGLISH.equals(lang) && code.startsWith("es-")) {
    		lang = Language.SPANISH_MEXICO;
    	}
    	return lang;
    }

    public static Language findByName(String name) {
    	Language lang = BY_NAME.getOrDefault(name, ENGLISH);
    	if(ENGLISH.equals(lang) && name.startsWith("SPANISH")) {
    		lang = Language.SPANISH_MEXICO;
    	}
    	return lang;
    }

    public String getFlagUnicode() {
        return flagUnicode;
    }
}