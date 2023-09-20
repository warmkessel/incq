package com.incq.util;

public class HtmlHelper {
	public static final String CRLF = "\r\n";
	public static String convertLongText(String input) {
		StringBuffer theReturn = new StringBuffer();
        String[] lines = splitOnCRLF(input);
		for(int x=0; x< lines.length; x++) {
			if(lines[x].length() > 200) {
				theReturn.append("<p>").append(lines[x]).append("</p>");
			}
			else if(lines[x].length() > 0) {
				theReturn.append("<h4>").append(lines[x]).append("</h4>");
			}
		}
		return theReturn.toString();
	}
	public static String convertLongJSON(String input) {
		StringBuffer theReturn = new StringBuffer("[");
        String[] lines = splitOnCRLF(input);
		boolean first = true;
        for(int x=0; x< lines.length; x++) {
			if(lines[x].length() < 200 && lines[x].trim().length() > 0) {
				if(first) {
					first = false;

				}
				else {
					theReturn.append(",");

				}
				theReturn.append("\"").append(lines[x]).append("\"");

			}
		}
		theReturn.append("]");

		return theReturn.toString();
	}
	public static String[] splitOnCRLF(String input) {
        if (input == null) {
            return new String[0]; // return an empty array if input is null
        }
        return input.split(CRLF);
    }
}
