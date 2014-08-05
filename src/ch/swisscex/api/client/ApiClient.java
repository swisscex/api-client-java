package ch.swisscex.api.client;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.swisscex.api.client.util.HMACSHA256;
import ch.swisscex.api.client.util.HttpHelper;

/**
 * @author neo
 */
public class ApiClient {
	
	private final static Logger log = LoggerFactory.getLogger(ApiClient.class);
	
	private final ApiConfig apiConfig;
	private Map<String, String> headers = new ConcurrentHashMap<>();
	private final AtomicLong nonce = new AtomicLong(System.currentTimeMillis());
	private final HMACSHA256 hmac;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public ApiClient(ApiConfig config) {
		apiConfig = config;
		hmac = new HMACSHA256(apiConfig.getPrivateKey());
	}

	public ApiConfig getApiConfig() {
		return apiConfig;
	}
	
	public String doGet(String method, Map<String, Object> params) {
		if(null == method) {
			throw new IllegalArgumentException("method missing");
		}
		StringBuffer baseQueryBuffer = prepareQuerString(params);
		String baseQueryString = baseQueryBuffer.toString();
		log.debug("query string [{}]", baseQueryString);
		String hash = hmac.hash(baseQueryString);
		log.debug("hash [{}]", hash);
		baseQueryBuffer.append("&hash=").append(hash);
		
		String fullRequestUrl = apiConfig.getHttpEndpoint() + method +  "?" + baseQueryBuffer.toString();
		log.debug("Requesting URL [{}]", fullRequestUrl);
		String response = HttpHelper.doGet(fullRequestUrl);
		log.debug("Response [{}]", response);
		return response;
	}
	
	public <T> T doGet(String method, Map<String, Object> params, Class<T> clazz) {
		String response = doGet(method, params);
		if(null != response && !response.isEmpty()) {
			try {
				T obj = mapper.readValue(response, clazz);
				return obj;
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	private StringBuffer prepareQuerString(Map<String, Object> params) {
		StringBuffer buf = new StringBuffer("apiKey=").append(apiConfig.getPublicKey())
				.append("&nonce=").append(nonce.getAndIncrement());
		if(null != params && !params.isEmpty()) {
			for(Entry<String, Object> entry : params.entrySet()) {
				buf.append("&").append(entry.getKey()).append("=").append(entry.getValue().toString());
			}
		}
		return buf;
	}
}