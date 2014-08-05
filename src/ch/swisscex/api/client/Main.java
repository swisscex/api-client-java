package ch.swisscex.api.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.swisscex.api.client.ui.MainWindow;
import ch.swisscex.api.model.QuoteListResponse;

/**
 * Main Class.
 * @author locorider
 */
public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		String fileName = "config.properties";
		if(args.length > 0) {
			fileName = args[0];
		}
		ApiPushClient apiPushClient = new ApiPushClient();
		ApiClient apiClient = null;
		try {
			ApiConfig config = new ApiConfig();
			config.load(fileName);
			apiPushClient.connect(config.getWebsocketEndpoint());
			apiClient = new ApiClient(config);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
//			apiPushClient.disconnect();
		}
		
		MainWindow win = new MainWindow(apiClient, apiPushClient);
		win.start();
	}
}