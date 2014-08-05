package ch.swisscex.api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

	public String		symbol;
	public BigDecimal	askPrice = BigDecimal.ZERO;
	public BigDecimal	bidPrice = BigDecimal.ZERO;
	public BigDecimal	low24 = BigDecimal.ZERO;
	public BigDecimal	high24 = BigDecimal.ZERO;
	public BigDecimal	volume24 = BigDecimal.ZERO;
	public BigDecimal	lastPrice = BigDecimal.ZERO;
	public BigDecimal	lastVolume = BigDecimal.ZERO;
	public BigDecimal	buyVolume = BigDecimal.ZERO;
	public BigDecimal	sellVolume = BigDecimal.ZERO;
	public BigDecimal	change = BigDecimal.ZERO;
	public float		percentalChange = 0.0f;
	public MarketStatus	marketStatus;
}