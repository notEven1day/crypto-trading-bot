package com.trading.bot.controller;

import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.service.CoinGeckoService;
import com.trading.bot.service.CurrencyService;
import com.trading.bot.service.PriceHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CryptoController {

    private CurrentlyTrading currentlyTrading;
    private final CurrencyService currencyService;
    private final PriceHistoryService priceHistoryService;

    public CryptoController(CurrencyService currencyService, CurrentlyTrading currentlyTrading, PriceHistoryService priceHistoryService) {
        this.currentlyTrading = currentlyTrading;
        this.currencyService = currencyService;
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/currencies/fetch")
    public String fetchAndStoreCurrencies() {
        currencyService.fetchAndStoreAllCurrencies();
        return "Currency fetch completed!";
    }

    @GetMapping("/currencies/all")
    public List<Map<String, Object>> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping("/select-crypto")
    public String selectCrypto(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String coingeckoId = payload.get("coingeckoId");

        currentlyTrading.setCoinName(name);
        currentlyTrading.setCoinGeckoId(coingeckoId);
        System.out.println(currentlyTrading.getCoinName());
        System.out.println(currentlyTrading.getCoinGeckoId());
        //TODO: We can implement this logic so we dont fetch everytime the info for the past year from the api. Can just fetch from last timestamp in db to now
        //Free api users can fetch only 365 days back
        priceHistoryService.backfillLastYearToYesterday();
        priceHistoryService.backfillYesterday();
        priceHistoryService.startTracking();
        return "Currently trading crypto updated! Price fetching started! Trading modes available";
    }
}
