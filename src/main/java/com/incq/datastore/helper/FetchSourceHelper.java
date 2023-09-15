package com.incq.datastore.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class FetchSourceHelper {
	static Logger logger = Logger.getLogger(FetchSourceHelper.class.getName());

	private static final String[] USER_AGENTS = {
			"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/80.0.3987.87 Chrome/80.0.3987.87 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.47 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/74.0",
			"Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.3",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/601.7.7 (KHTML, like Gecko) Version/9.1.2 Safari/601.7.7",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0",
			"Mozilla/5.0 (X11; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1 Safari/605.1.15",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/58.0",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"};

	public static String fetchContent(String urlString) throws Exception {
		List<String> agents = new ArrayList<String>();
		for (int x = 0; x < USER_AGENTS.length; x++) {
			agents.add(USER_AGENTS[x]);
		}

		return fetchContent(urlString, agents);

	}

	public static String fetchContent(String urlString, List<String> agents) throws Exception {
		String theReturn = "";
		if (agents.size() > 0) {

			URL url = new URL(urlString);

			// Open HTTP connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set random User-Agent
			Random random = new Random();
			int num = random.nextInt((agents.size()));

			String randomUserAgent = agents.get(num);
			connection.setRequestProperty("User-Agent", randomUserAgent);
			connection.setRequestProperty("Accept", "text/plain; charset=UTF-8");
			connection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			StringBuffer content = new StringBuffer();

			// Check for Gzip encoding and wrap the input stream if needed
			String contentEncoding = connection.getHeaderField("Content-Encoding");
			InputStreamReader inputStreamReader;
			if ("gzip".equalsIgnoreCase(contentEncoding)) {
				inputStreamReader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()),
						StandardCharsets.UTF_8);
			} else {
				inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
			}

			// Read the response
			BufferedReader in = new BufferedReader(inputStreamReader);
			String line;
			while ((line = in.readLine()) != null) {
				content.append(line).append("\n");
			}

			theReturn = content.toString();

			if (theReturn.contains("robot")) {
				agents.remove(num);
				theReturn = fetchContent(urlString, agents);
			}
		}
		return theReturn;
	}

	public static void main(String[] args) {
		try {
			String content = fetchContent("https://www.example.com");
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}