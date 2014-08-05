package ch.swisscex.api.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;

import ch.swisscex.api.client.ApiClient;
import ch.swisscex.api.model.OHLCResponse;

public class OHLCPane extends JPanel {
	private static final long serialVersionUID = -5082484384682999810L;
	
	private final ApiClient apiClient;
	
	public OHLCPane(ApiClient client) {
		this.apiClient = client;
	}
	
	public void loadData(String symbol) {
		OHLCResponse ohlcData = apiClient.doGet("quote/" + symbol + "/ohlc", null, OHLCResponse.class);
		if(null != ohlcData && null != ohlcData.data) {
			final int length = ohlcData.data.length;
			OHLCDataItem[] ohlcDataItems = new OHLCDataItem[length];
			DateAxis domainAxis = new DateAxis("Date");
			NumberAxis rangeAxis = new NumberAxis("Price");
			CandlestickRenderer renderer = new CandlestickRenderer();
			
			int idx = 0;
			for(Object[] ohlcEntry : ohlcData.data) {
				OHLCDataItem item = new OHLCDataItem(new Date((long) ohlcEntry[0]), getValue(ohlcEntry[1]),  getValue(ohlcEntry[2]), 
						 getValue(ohlcEntry[3]),  getValue(ohlcEntry[4]),  getValue(ohlcEntry[5]));
				ohlcDataItems[idx++] = item;
			}
			DefaultOHLCDataset dataset = new DefaultOHLCDataset(symbol, ohlcDataItems);
			XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

	        final long ONE_DAY = 24 * 60 * 60 * 1000;
	        XYLineAndShapeRenderer maRenderer = new XYLineAndShapeRenderer(true, false);
	        XYDataset              maSataset  = MovingAverage.createMovingAverage(dataset, "MA", 30 * ONE_DAY, 0);
	        mainPlot.setRenderer(1, maRenderer);
	        mainPlot.setDataset (1, maSataset);
			
//			final JFreeChart chart = ChartFactory.createCandlestickChart(symbol, "Time", "OHLC", dataset, false);
//			chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
			
			renderer.setSeriesPaint(0, Color.BLACK);
			rangeAxis.setAutoRangeIncludesZero(false);
			domainAxis.setTimeline(SegmentedTimeline.newFifteenMinuteTimeline());
			
			JFreeChart chart = new JFreeChart(symbol, null, mainPlot, false);
			final ChartPanel chartPanel = new ChartPanel(chart, false);
			chartPanel.setMouseWheelEnabled(true);
			chartPanel.setPreferredSize(new Dimension(800, 600));
			add(chartPanel, BorderLayout.CENTER);
		}
	}
	
	private double getValue(Object val) {
		if(val instanceof Integer) {
			return ((Integer) val).doubleValue();
		} else if(val instanceof Double) {
			return ((Double) val);
		} else {
			return 0;
		}
	}
}