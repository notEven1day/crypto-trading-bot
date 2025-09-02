package com.trading.bot.service;

import com.trading.bot.config.BotState;
import com.trading.bot.config.CurrentTradingTime;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BotService {
    private final CurrentTradingTime currentTradingTime;
    private final BotState botState;
    public BotService(CurrentTradingTime currentTradingTime, BotState botState) {
        this.currentTradingTime = currentTradingTime;
        this.botState = botState;
    }
    public void setMode(String mode) {
        if ("TRAINING".equalsIgnoreCase(mode)) {
            currentTradingTime.setTrainingMode(true);
            currentTradingTime.setCurrentTime(LocalDateTime.now().minusDays(300));
        } else {
            currentTradingTime.setTrainingMode(false);
            currentTradingTime.setCurrentTime(LocalDateTime.now());
        }
    }

    public boolean isBotTrading(){
        return botState.isTrading();
    }

    public LocalDateTime getCurrentTradingTime() {
        return currentTradingTime.getCurrentTime();
    }

}
