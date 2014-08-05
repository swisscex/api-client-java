package ch.swisscex.api.client;

import ch.swisscex.api.model.Quote;

public interface PushListener {

	public void onQuoteUpdate(Quote quote);
	
	public void onOrderbookUpdateBuy();
	
	public void onOrderbookUpdateSell();
	
	public void onTradePush();
	
	public void onExectuionReport();
}