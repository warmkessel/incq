package com.incq.constants;

import java.util.HashMap;
import java.util.Map;

public enum Domains {
	WWW("www"),
	BRUTAL("brutal");

	private static final Map<String, Domains> BY_NAME = new HashMap<>();
	static {
		for (Domains lang : values()) {
			BY_NAME.put(lang.name, lang);
		}
	}

	public final String name;
	

	
	Domains(String name) {
		this.name = name;
	}

	

	public static Domains findByName(String name) {
		Domains lang = BY_NAME.getOrDefault(name, WWW);
		return lang;
	}
}