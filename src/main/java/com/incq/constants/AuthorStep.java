package com.incq.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public enum AuthorStep {
	FAIL("fail"), // "Failure"
	STEP1("step1"), // "Short Description"
	STEP2("step2"), // "Long Description"
	STEP3("step3");// "Enable"

	static Logger logger = Logger.getLogger(AuthorStep.class.getName());

	AuthorStep(String name) {
		this.name = name;
	}

	public final String name;

	private static final Map<String, AuthorStep> BY_NAME = new HashMap<>();
	
	static {
        for (AuthorStep lang : values()) {
            BY_NAME.put(lang.name, lang);
        }
    }
	
	public static AuthorStep findByName(String name) {
		return BY_NAME.getOrDefault(name, FAIL);
	}
	
	public AuthorStep next() {
		   // Get the list of all enum values
		AuthorStep[] allSteps = AuthorStep.values();

        // Get the ordinal value of the current enum instance
        int ordinal = this.ordinal();

        // Find the next ordinal. If it goes out of range, loop back to the start
        int nextOrdinal = (ordinal + 1) % allSteps.length;

        // Return the next enum value
        return allSteps[nextOrdinal];
    }

}