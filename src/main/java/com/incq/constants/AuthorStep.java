package com.incq.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public enum AuthorStep {
	FAIL("fail", "Failure"),
	STEP1("step1", "Suggest an Authors Name"),
	STEP2("step2", "Suggest some Tags"), 
	STEP3("step3", "Suggest some Tags"), 
	STEP4("step4", "Suggest Long Description"), 
	STEP5("step5", "Suggest Short Description"), 
	STEP6("step6", "translate Name"), 
	STEP7("step7", "translate Short Description"), 
	STEP8("step8", "translate Long Description"), 
	STEP9("step9", "translate Tags"), 
	STEP10("step10", "Enable");
	

	static Logger logger = Logger.getLogger(AuthorStep.class.getName());

	AuthorStep(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public final String name;
	public final String desc;

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