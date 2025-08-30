package com.trading.bot.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CurrentTradingTime {

    private LocalDateTime currentTime;
    private boolean trainingMode;

    public CurrentTradingTime() {
        this.currentTime = LocalDateTime.now(); // default to real-time
        this.trainingMode = false;               // default is live mode
    }

    // Getters and setters
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isTrainingMode() {
        return trainingMode;
    }

    public void setTrainingMode(boolean trainingMode) {
        this.trainingMode = trainingMode;
        if (!trainingMode) {
            this.currentTime = LocalDateTime.now(); // reset to real-time
        }
    }

    // Utility method to advance time (for backtesting)
    public void advanceTimeBySeconds(long seconds) {
        this.currentTime = this.currentTime.plusSeconds(seconds);
    }
}
