package com.trading.bot.config;

import org.springframework.stereotype.Component;

@Component
public class CurrentlyTrading {

    private String coin = "BTC"; // default value

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
