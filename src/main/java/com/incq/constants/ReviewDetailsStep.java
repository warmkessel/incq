package com.incq.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public enum ReviewDetailsStep {
	FAIL("fail"), // "Failure"
	STEP1("step1"), // Translate Introduction"
	STEP2("step2"), // Translate Review"
	STEP3("step3"), // Translate Conclusion"
	STEP4("step4"), // Translate Summary"
	STEP5("step5"), // Translate Title"
	STEP6("step6"), // Translate Name"
	STEP7("step7"), // Translate Description"
	STEP8("step8"); // Enable

	static Logger logger = Logger.getLogger(ReviewDetailsStep.class.getName());

	ReviewDetailsStep(String name) {
		this.name = name;
	}

	public final String name;

	private static final Map<String, ReviewDetailsStep> BY_NAME = new HashMap<>();
	
	static {
        for (ReviewDetailsStep lang : values()) {
            BY_NAME.put(lang.name, lang);
        }
    }
	
	public static ReviewDetailsStep findByName(String name) {
		return BY_NAME.getOrDefault(name, FAIL);
	}
	
	public ReviewDetailsStep next() {
		   // Get the list of all enum values
		ReviewDetailsStep[] allSteps = ReviewDetailsStep.values();

        // Get the ordinal value of the current enum instance
        int ordinal = this.ordinal();

        // Find the next ordinal. If it goes out of range, loop back to the start
        int nextOrdinal = (ordinal + 1) % allSteps.length;

        // Return the next enum value
        return allSteps[nextOrdinal];
    }

}