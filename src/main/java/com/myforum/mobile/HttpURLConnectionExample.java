package com.myforum.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


@SuppressWarnings("deprecation")
public class HttpURLConnectionExample {
	 
	private final String USER_AGENT = "Mozilla/5.0";
 
	public static void main(String[] args) throws Exception {
 
		HttpURLConnectionExample http = new HttpURLConnectionExample();
 
		http.sendAll();
 
	}
 
	// HTTP GET request
	private void sendAll() throws Exception {

		//sendGet( "http://127.0.0.1:8080/mobile/?action=getmessagecategories" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=getmessages&codemessagecategory=3" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=getmessage&codemessage=1" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=getusername" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=isadministrator" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=getforumname" );
		//sendGet( "http://127.0.0.1:8080/mobile/?action=getavatar&codeforumuser=1" );
		sendPost( "http://127.0.0.1:8080/mobile/?action=getavatar&codeforumuser=1" );
	}
	// HTTP GET request
	
	@SuppressWarnings("unused")
	private void sendGet( String url) throws Exception {
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
	
	// HTTP POST request
	private void sendPost(String url) throws Exception {
 	
		 
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
 
		// add header
		post.setHeader("User-Agent", USER_AGENT);
 
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("action", "getavatar"));
		urlParameters.add(new BasicNameValuePair("codeforumuser", "1"));
 
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
 
		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
	}
}