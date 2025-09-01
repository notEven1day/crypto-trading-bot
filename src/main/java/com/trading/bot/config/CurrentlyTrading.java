package com.trading.bot.config;

import org.springframework.stereotype.Component;

@Component
public class CurrentlyTrading {

    private String coinName = "Bitcoin";       // default coin name
    private String coinGeckoId = "bitcoin";    // default CoinGecko ID

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinGeckoId() {
        return coinGeckoId;
    }

    public void setCoinGeckoId(String coinGeckoId) {
        this.coinGeckoId = coinGeckoId;
    }
}
