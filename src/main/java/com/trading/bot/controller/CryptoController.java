package com.trading.bot.controller;

import com.trading.bot.service.CoinGeckoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class CryptoController {

    private final CoinGeckoService coinGeckoService;

    public CryptoController(CoinGeckoService coinGeckoService) {
        this.coinGeckoService = coinGeckoService;
    }

    @GetMapping("/bitcoin/history")
    public Map<String, Object> getBitcoinHistory() {
        return coinGeckoService.getBitcoinLastYear();
    }
}
