package com.trading.bot.controller;

import com.trading.bot.service.CoinGeckoService;
import com.trading.bot.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class CryptoController {


    private final CurrencyService currencyService;

    public CryptoController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies/fetch")
    public String fetchAndStoreCurrencies() {
        currencyService.fetchAndStoreAllCurrencies();
        return "Currency fetch completed!";
    }
}
