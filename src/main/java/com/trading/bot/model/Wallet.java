package com.trading.bot.model;

import java.math.BigDecimal;

public class Wallet {
    private Long id;
    private BigDecimal capital;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getCapital() { return capital; }
    public void setCapital(BigDecimal capital) { this.capital = capital; }
}
