package com.trading.bot.config;

import org.springframework.stereotype.Component;

@Component
public class BotState {

    private boolean isTrading = false; // default stopped

    public boolean isTrading() {
        return isTrading;
    }

    public void startTrading() {
        this.isTrading = true;
    }

    public void stopTrading() {
        this.isTrading = false;
    }
}
