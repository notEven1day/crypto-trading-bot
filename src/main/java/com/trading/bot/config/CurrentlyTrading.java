package com.trading.bot.config;

import org.springframework.stereotype.Component;

@Component
public class CurrentlyTrading {

    private String coinName = "Bitcoin"; // default value

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}

