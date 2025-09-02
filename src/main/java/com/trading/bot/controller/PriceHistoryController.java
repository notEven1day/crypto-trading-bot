package com.trading.bot.controller;

import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.service.PriceHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    //closest we can get to 35 seconds price update from coingecko free api is 1 min from yesterday
    @PostMapping("/backfill/yesterday")
    public String backfillYesterday() {
        priceHistoryService.backfillYesterday();
        return "Backfill started for yesterday for currently trading coin.";
    }

}
