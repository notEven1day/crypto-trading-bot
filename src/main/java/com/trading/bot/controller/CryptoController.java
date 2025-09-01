package com.trading.bot.controller;

import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.service.CoinGeckoService;
import com.trading.bot.service.CurrencyService;
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

    public CryptoController(CurrencyService currencyService, CurrentlyTrading currentlyTrading) {
        this.currentlyTrading = currentlyTrading;
        this.currencyService = currencyService;
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
//        System.out.println(currentlyTrading.getCoinName());
//        System.out.println(currentlyTrading.getCoinGeckoId());
        return "Currently trading crypto updated!";
    }
}
