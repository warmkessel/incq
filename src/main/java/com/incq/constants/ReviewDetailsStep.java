package com.incq.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public enum ReviewDetailsStep {
	FAIL("fail", "Failure"), // "Failure"
	STEP1("step1", "Translate Introduction"), // Translate Introduction
	STEP2("step2", "Translate Description"), // Translate Description
	STEP3("step3", "Translate Conclusion"), // Translate Conclusion
	STEP4("step4", "Translate Summary"), // Translate Summary
	STEP5("step5", "Translate Title"), // Translate Title
	STEP6("step6", "Translate Name"), // Translate Name
	STEP7("step7", "Translate Call"), // Translate Call
	STEP8("step8", "Translate Review"), // Translate Review
	STEP9("step9", "Translate Tags"), // Translate Tags
	STEP10("step10", "Translate Meta"), // Translate Meta
	STEP11("step11", "Enable"), // Enable
	STEP12("step12", "Post To Facebook"), // PostToFacebook
	STEP13("step13", "Post To IndexNow"); // IndexNow

	static Logger logger = Logger.getLogger(ReviewDetailsStep.class.getName());

	ReviewDetailsStep(String name, String desc ) {
		this.name = name;
		this.desc = desc;
	}

	public final String name;
	public final String desc;

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