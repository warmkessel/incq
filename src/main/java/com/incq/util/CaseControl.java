package com.incq.util;

import org.jsoup.Jsoup;

public class CaseControl {
		
	public static String cleanHTML(String str) {
		 if (str == null || str.length() == 0) {
		        return "";
		    }
		 return Jsoup.parse(str).text();
	}
	
	public static String capAndLb(String str) {
		return toHtml(capFirstLetter(str));
	}
	
	public static String capAndUnderscore(String str) {
		return replaceUnderscore(capFirstLetter(str));
	}
	public static String toHtmlParagraph(String paragraph) {
	    return "<p>" + paragraph.trim() + "</p>";
	}
	
	public static String toHtml(String text) {
	    if (text == null || text.length() == 0) {
	        return "";
	    }
	    String[] paragraphs = text.split("\n");
	    StringBuilder htmlBuilder = new StringBuilder();
	    for (String paragraph : paragraphs) {
	        htmlBuilder.append(toHtmlParagraph(paragraph)).append("\n");
	    }
	    return htmlBuilder.toString();
	}
	
	
	public static String capFirstLetter(String str) {
		if(null == str || str.length() == 0) {
			return "";
		}
		return  str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	public static String replaceUnderscore(String str) {
		if(null == str || str.length() == 0) {
			return "";
		}
		return  str.replaceAll("_", " ");
	}
	public static String replaceHyphen(String str) {
		if(null == str || str.length() == 0) {
			return "";
		}
		return  str.replaceAll("-", "_");
	}
}
