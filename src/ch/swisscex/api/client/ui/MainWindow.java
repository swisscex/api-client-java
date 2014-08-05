package ch.swisscex.api.client.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ch.swisscex.api.client.ApiClient;
import ch.swisscex.api.client.ApiPushClient;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 6984798735118484777L;
	
	private final ApiClient apiClient;
	private final ApiPushClient apiPushClient;
	
	public MainWindow(ApiClient client, ApiPushClient apiPushClient) {
		super("SWISSCEX - Trading Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.apiClient = client;
		this.apiPushClient = apiPushClient;
	}

	public void start() {
		JTabbedPane tabbedPane = new JTabbedPane();
		QuoteList quoteList = new QuoteList(apiClient);
		JScrollPane scrollPane = new JScrollPane(quoteList);
		getContentPane().add(scrollPane, BorderLayout.WEST);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		setSize(1024, 768);
		pack();
		setVisible(true);
		
		quoteList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		quoteList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getFirstIndex() > -1 && !e.getValueIsAdjusting()) {
					String symbol = quoteList.getModel().getValueAt(e.getFirstIndex(), 0).toString();
					OHLCPane pane = new OHLCPane(apiClient);
					JPanel panel = new JPanel();
					panel.add(pane, BorderLayout.CENTER);
					tabbedPane.addTab(symbol, panel);
					pane.loadData(symbol);
					
				}
			}
		});
	}
}