package com.trading.bot.strategy;

import com.trading.bot.config.CurrentTradingTime;
import com.trading.bot.enums.TradingDecision;
import com.trading.bot.repository.PriceHistoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovingAverageCrossoverStrategy {

    private final PriceHistoryRepository priceHistoryRepository;
    public MovingAverageCrossoverStrategy(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public TradingDecision evaluate(Long coinId, LocalDateTime currentTradingTime) {
        Double smaShort = priceHistoryRepository.calculateSMA(coinId, 3, currentTradingTime); // short-term
        Double smaLong  = priceHistoryRepository.calculateSMA(coinId, 10, currentTradingTime); // long-term
        System.out.println(smaLong);
        System.out.println(smaShort);
        if (smaShort == null || smaLong == null) {
            return TradingDecision.HOLD; // not enough data
        }

        if (smaShort > smaLong) {
            return TradingDecision.BUY;
        } else if (smaShort < smaLong) {
            return TradingDecision.SELL;
        } else {
            return TradingDecision.HOLD;
        }
    }
}
