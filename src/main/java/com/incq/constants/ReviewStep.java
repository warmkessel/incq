package com.incq.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public enum ReviewStep {
	FAIL("fail"), // "Failure"
	STEP1("step1"), // Fetch the Source
	STEP2("step2"), // Select a Author Profile
	STEP3("step3"), // Extract Title and Media from source
	STEP4("step4"), // Determine the Meta Tags
	STEP5("step5"), // Determine the Tags
	STEP6("step6"), // Write the Review Body
	STEP7("step7"), // Write the introductions
	STEP8("step8"), // Write the Conclusion
	STEP9("step9"), // Write a summary
	STEP10("step10"), // Write Name
	STEP11("step11"), // Write Slug
	STEP12("step12"), // Write Description
	STEP13("step13"); // Mark the Review Active

	static Logger logger = Logger.getLogger(ReviewStep.class.getName());

	ReviewStep(String name) {
		this.name = name;
	}

	public final String name;

	private static final Map<String, ReviewStep> BY_NAME = new HashMap<>();
	
	static {
        for (ReviewStep lang : values()) {
            BY_NAME.put(lang.name, lang);
        }
    }
	
	public static ReviewStep findByName(String name) {
		return BY_NAME.getOrDefault(name, FAIL);
	}
	
	public ReviewStep next() {
		   // Get the list of all enum values
		ReviewStep[] allSteps = ReviewStep.values();

        // Get the ordinal value of the current enum instance
        int ordinal = this.ordinal();

        // Find the next ordinal. If it goes out of range, loop back to the start
        int nextOrdinal = (ordinal + 1) % allSteps.length;

        // Return the next enum value
        return allSteps[nextOrdinal];
    }

}