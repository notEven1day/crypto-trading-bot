package com.trading.bot.service;

import com.trading.bot.repository.HoldingStockRepository;
import com.trading.bot.repository.TradeRepository;
import com.trading.bot.repository.PriceHistoryRepository;
import com.trading.bot.config.CurrentlyTrading;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HoldingStockService {

    private final HoldingStockRepository holdingRepo;
    private final TradeRepository tradeRepo;
    private final PriceHistoryRepository priceHistoryRepo;
    private final CurrentlyTrading currentlyTrading;

    private static final double INVESTMENT_USD = 100.0;

    public HoldingStockService(HoldingStockRepository holdingRepo,
                               TradeRepository tradeRepo,
                               PriceHistoryRepository priceHistoryRepo,
                               CurrentlyTrading currentlyTrading) {
        this.holdingRepo = holdingRepo;
        this.tradeRepo = tradeRepo;
        this.priceHistoryRepo = priceHistoryRepo;
        this.currentlyTrading = currentlyTrading;
    }

    //TODO: need to check if we got enough funds before buying. if we dont have continue
    public void buy(Long coinId) {
        // get latest price of the coin from price_history
        Double latestPrice = priceHistoryRepo.findLatestPrice(coinId);
        if (latestPrice == null) return;

        // calculate quantity based on 100 USD
        double quantity = INVESTMENT_USD / latestPrice;

        Map<String, Object> holding = holdingRepo.findByCoinId(coinId);
        System.out.println(holding);
        if (holding == null) {
            holdingRepo.insert(coinId, latestPrice, quantity);
        } else {
            double oldQty = ((Number) holding.get("quantity")).doubleValue();
            double oldAvg = ((Number) holding.get("average_price")).doubleValue();
            double newQty = oldQty + quantity;
            double newAvg = ((oldAvg * oldQty) + (latestPrice * quantity)) / newQty;
            holdingRepo.update(coinId, newAvg, newQty);
        }

        tradeRepo.saveTrade(coinId, latestPrice, quantity, "BUY", null, "SUCCESS");
    }

    public void sell(Long coinId) {
        Double latestPrice = priceHistoryRepo.findLatestPrice(coinId);
        if (latestPrice == null) return;

        Map<String, Object> holding = holdingRepo.findByCoinId(coinId);
        if (holding == null) return; // nothing to sell

        double oldQty = ((Number) holding.get("quantity")).doubleValue();
        double oldAvg = ((Number) holding.get("average_price")).doubleValue();

        if (oldQty <= 0) return;

        // sell ALL holdings
        double quantity = oldQty;
        double newQty = 0.0;
        holdingRepo.update(coinId, oldAvg, newQty);

        double profit = (latestPrice - oldAvg) * quantity;
        tradeRepo.saveTrade(coinId, latestPrice, quantity, "SELL", profit, "COMPLETED");
    }
}
