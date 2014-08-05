package ch.swisscex.api.client.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple HTTP Helper class that allows to do GET and POST requests
 * @author neo
 */
public class HttpHelper {
	
	private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);
	
	public static String doPost(URL postURL, String urlParams) throws IOException {
		StringBuffer responseString = new StringBuffer();
		
		HttpURLConnection httpConnection = (HttpURLConnection) postURL.openConnection();
		httpConnection.setRequestMethod("POST");
		if(null != urlParams) {
			httpConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();
		}
		int responseCode = httpConnection.getResponseCode();
		if(200 == responseCode) {
			BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String inputLine = null;
			while((inputLine = br.readLine()) != null) {
				responseString.append(inputLine);
			}
		}
		return responseString.toString();
	}

	public static String doPost(String url, String urlParams) {
		String response = null;
		try {
			URL postURL = new URL(url);
			response = doPost(postURL, urlParams);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return response;
	}
	
	public static String doGet(String target) {
		String response = null;
		try {
			StringBuffer buf = new StringBuffer();
			URL url = new URL(target);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String readLine = null;
			while((readLine = in.readLine()) != null) {
				buf.append(readLine);
			}
			response = buf.toString();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return response;
	}
}