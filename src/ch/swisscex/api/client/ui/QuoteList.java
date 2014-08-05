package ch.swisscex.api.client.ui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ch.swisscex.api.client.ApiClient;
import ch.swisscex.api.model.Quote;
import ch.swisscex.api.model.QuoteListResponse;

public class QuoteList extends JTable {
	private static final long serialVersionUID = 7306546064925318705L;

	public static final String[] columnNames = { "Instr.", "Bid", "Ask" };
	
	private DefaultTableModel tableModel = null;
	private final ApiClient apiClient;
	
	public QuoteList(ApiClient apiClient) {
		super();
		this.apiClient = apiClient;
		tableModel = new DefaultTableModel(columnNames, 0);
		loadData();
	}
	
	public void loadData() {
		QuoteListResponse response = apiClient.doGet("quote", null, QuoteListResponse.class);
		tableModel.setRowCount(0);
		setModel(tableModel);
		if(null != response && null != response.data) {
			for(Quote quote : response.data.values()) {
				Object[] data = new Object[] { quote.symbol, quote.bidPrice, quote.askPrice };
				tableModel.addRow(data);
			}
		}
	}
}
