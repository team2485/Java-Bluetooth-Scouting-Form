package org.first.team2485.scoutingform.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("unused")
public class HTTPUtils {

	private static String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	
	public static String getUserAgent() {
		return USER_AGENT;
	}
	
	public static void setUserAgent(String UserAgent) {
		USER_AGENT = UserAgent;
	}
	
	public static String sendGet(String url) throws Exception {
		return sendGet(url, new String[0], new String[0]);
	}
	
	public static String sendGet(String url, String[] headers, String[] params) throws Exception {
		
		String fullUrl = new String(url);
		if(headers.length>0) {
			fullUrl = fullUrl + "?";
		}
		for (int i = 0; i < headers.length; i++) {
			if(i != 0) {
				fullUrl = fullUrl + "&";
			}
			
			fullUrl = fullUrl + headers[i] + "=" + params[i];
		}
		
		URL obj = new URL(fullUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setConnectTimeout(30000);
		
		// optional default is GET
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//return result
		return response.toString();

	}
	
	public static String sendPost(String url) throws Exception {
		return sendPost(url, new String[0], new String[0]);
	}
	
	// HTTP POST request
	public static String sendPost(String url, String[] headers, String[] params) throws Exception {
				
		URL obj = new URL(url);
		URLConnection tempCon = obj.openConnection();

		System.out.println("here");
		
		tempCon.setConnectTimeout(30000);
		
		tempCon.setReadTimeout(30000);
		
		System.out.println("here");
		
		HttpsURLConnection con = ((HttpsURLConnection) tempCon);
		
		//add reuqest headeroi
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "";
		for(int i = 0; i < headers.length; i++) {
			if(i!=0) {
				urlParameters = urlParameters + "&";
			}
			
			urlParameters = urlParameters + headers[i] + "=" + params[i];
		}
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		return response.toString();

	}

}