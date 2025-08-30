package com.trading.bot.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Trade {
    private Long id;
    private Long currencyId;
    private BigDecimal price;
    private BigDecimal quantity;
    private String action; // BUY / SELL
    private Timestamp timestamp;
    private BigDecimal profit;
    private String status; // e.g. SUCCESS, FAILED

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCurrencyId() { return currencyId; }
    public void setCurrencyId(Long currencyId) { this.currencyId = currencyId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
