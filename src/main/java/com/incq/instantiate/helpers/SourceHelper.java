package com.incq.instantiate.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SourceHelper {
    
	static Logger logger = Logger.getLogger(SourceHelper.class.getName());

	private String htmlSource;

    public SourceHelper(String htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String fetchTitle() {
        Document doc = Jsoup.parse(htmlSource);
        Element titleElement = doc.selectFirst("span#productTitle.a-size-large.product-title-word-break");

        if (titleElement != null) {
            return titleElement.text();
        }
        else {
        	logger.log(Level.SEVERE, "Title not found " + htmlSource);
        	return "Title not found";
        }
    }

    public String[] fetchImages() {
        Document doc = Jsoup.parse(htmlSource);
        Element altImagesDiv = doc.selectFirst("div#altImages.a-fixed-left-grid-col.a-col-left");

        if (altImagesDiv != null) {
            Elements imgElements = altImagesDiv.select("img");
            List<String> imgTags = new ArrayList<>();

            for (Element img : imgElements) {
                imgTags.add(img.toString());
            }

            return imgTags.toArray(new String[0]);
        }
        return new String[] {"Images not found"};
    }

    public static void main(String[] args) {
        String htmlSource = "Your HTML source here";
        SourceHelper sourceHelper = new SourceHelper(htmlSource);

        String title = sourceHelper.fetchTitle();
        String[] images = sourceHelper.fetchImages();

        System.out.println("Title: " + title);
        System.out.println("Images:");
        for (String img : images) {
            System.out.println(img);
        }
    }
}
