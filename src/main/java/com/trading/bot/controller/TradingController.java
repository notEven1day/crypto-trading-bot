package com.trading.bot.controller;

import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.dto.TradeDto;
import com.trading.bot.repository.CurrencyRepository;
import com.trading.bot.service.TradingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bot")
public class TradingController {

    private final TradingService tradingService;
    private final CurrencyRepository currencyRepository;
    private final CurrentlyTrading currentlyTrading;

    public TradingController(TradingService tradingService, CurrencyRepository currencyRepository, CurrentlyTrading currentlyTrading) {
        this.tradingService = tradingService;
        this.currencyRepository = currencyRepository;
        this.currentlyTrading = currentlyTrading;
    }

    @PostMapping("/start")
    public String startBot() {
        tradingService.startTrading(currencyRepository.getCoinIdByCoingeckoId(currentlyTrading.getCoinGeckoId()));
        return "Trading started!";
    }

    @PostMapping("/stop")
    public String stopBot() {
        tradingService.stopTrading();
        return "Trading stopped!";
    }

    @GetMapping
    public List<TradeDto> getTrades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tradingService.getTrades(page, size);
    }






}
