package ch.swisscex.api.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

/**
 * @author neo
 */
public class ApiPushClient {
	
	private static final Logger log = LoggerFactory.getLogger(ApiPushClient.class);

	private Socket socket = null;
	private List<PushListener> listeners = new ArrayList<>();
	
	public void connect(String endpoint) {
		try {
			log.info("Going to connect to [{}]", endpoint);
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			socket = IO.socket(endpoint);
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... arg0) {
					log.info("Got connection");
				}
			})
			.on("QUOTE_UPDATE", new Emitter.Listener() {
				public void call(Object... arg0) {
					log.info("Got QUOTE_UPDATE [{}]", arg0);
				}
			})
			.on("ORDERBOOK:UPDATE:BUY", new Emitter.Listener() {
				public void call(Object... arg0) {
					log.info("Got ORDERBOOK:UPDATE:BUY [{}]", arg0);
				}
			})
			.on("ORDERBOOK:UPDATE:SELL", new Emitter.Listener() {
				public void call(Object... arg0) {
					log.info("Got ORDERBOOK:UPDATE:SELL [{}]", arg0);
				}
			});
			socket.connect();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void disconnect() {
		if(null != socket) {
			try {
				socket.disconnect();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public void addListener(PushListener listener) {
		if(null != listener) {
			listeners.add(listener);
		}
	}
}