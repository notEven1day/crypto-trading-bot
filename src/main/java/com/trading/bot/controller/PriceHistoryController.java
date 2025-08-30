package com.trading.bot.controller;

import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.service.PriceHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceHistoryController {

    private CurrentlyTrading currentlyTrading;
    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService, CurrentlyTrading currentlyTrading) {
        this.priceHistoryService = priceHistoryService;
        this.currentlyTrading = currentlyTrading;
    }

    // ✅ Live mode endpoint
    @GetMapping("/price/live")
    public String getLivePrice() {
        priceHistoryService.startTracking();
        return "Tracking started for " + currentlyTrading.getCoinName() + "";
    }
}
