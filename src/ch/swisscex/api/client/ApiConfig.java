package ch.swisscex.api.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ApiConfig extends Properties {
	private static final long serialVersionUID = -4326239961280543864L;
	
	public static final String PROP_PRIVATE_KEY = "api.key.private";
	public static final String PROP_PUBLIC_KEY = "api.key.public";
	public static final String PROP_ENDPOINT_HTTP = "api.endpoint.http";
	public static final String PROP_ENDPOINT_WS = "api.endpoint.ws";
	
	public void load(String fileName) throws IOException {
		load(new File(fileName));
	}
	
	public void load(File file) throws IOException {
		FileReader fr = new FileReader(file);
		load(fr);
	}
	
	public String getPrivateKey() {
		return getProperty(PROP_PRIVATE_KEY);
	}
	
	public String getPublicKey() {
		return getProperty(PROP_PUBLIC_KEY);
	}
	
	public String getHttpEndpoint() {
		return getProperty(PROP_ENDPOINT_HTTP, "http://api.swisscex.com/v2/");
	}

	public String getWebsocketEndpoint() {
		return getProperty(PROP_ENDPOINT_WS, "https://stream.swisscex.com:444");
	}
}