package com.trading.bot.service;

import com.trading.bot.component.CurrentTradingTime;
import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.repository.CurrencyRepository;
import com.trading.bot.repository.PriceHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PriceHistoryService {

    private final CoinGeckoService coinGeckoService;
    private final CurrentTradingTime currentTradingTime;
    private final CurrentlyTrading currentlyTrading;
    private final CurrencyRepository currencyRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryService(CoinGeckoService coinGeckoService,
                               CurrentTradingTime currentTradingTime,
                               CurrentlyTrading currentlyTrading,
                               CurrencyRepository currencyRepository,
                               PriceHistoryRepository priceHistoryRepository) {
        this.coinGeckoService = coinGeckoService;
        this.currentTradingTime = currentTradingTime;
        this.currentlyTrading = currentlyTrading;
        this.currencyRepository = currencyRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public void startTracking() {
        String coinName = currentlyTrading.getCoinName(); // human-readable name
        Long coinId = currencyRepository.getCoinIdByName(coinName); // DB id
        String coinGeckoId = currencyRepository.getCoinGeckoIdByName(coinName); // CoinGecko API id

        if (currentTradingTime.isTrainingMode()) {
            fetchHistoricalForTraining(coinId, coinGeckoId);
        } else {
            startRealTimePolling(coinId, coinGeckoId);
        }
    }

    // Training / Backtesting
    private void fetchHistoricalForTraining(Long coinId, String coinGeckoId) {
        LocalDateTime fromTime = currentTradingTime.getCurrentTime();
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> response = coinGeckoService.getPriceHistory(coinGeckoId, fromTime, now);
        List<List<Object>> prices = (List<List<Object>>) response.get("prices");

        if (prices != null) {
            for (List<Object> entry : prices) {
                long timestampMs = ((Number) entry.get(0)).longValue();
                double price = ((Number) entry.get(1)).doubleValue();
                LocalDateTime ts = Instant.ofEpochMilli(timestampMs)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                priceHistoryRepository.insertPrice(coinId, price, ts);
            }
        }

        // Move simulated clock forward
        currentTradingTime.setCurrentTime(now);
    }

    // Live polling
    private void startRealTimePolling(Long coinId, String coinGeckoId) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                Map<String, Object> response = coinGeckoService.getLatestPrice(coinGeckoId);
                System.out.println(response);

                // Nested JSON: { "bitcoin": { "usd": 108524 } }
                Map<String, Object> coinData = (Map<String, Object>) response.get(coinGeckoId.toLowerCase());
                if (coinData == null || coinData.get("usd") == null) {
                    System.err.println("Invalid API response for coin: " + coinGeckoId);
                    return;
                }

                double price = ((Number) coinData.get("usd")).doubleValue();
                priceHistoryRepository.insertPrice(coinId, price, LocalDateTime.now());

            } catch (Exception e) {
                System.err.println("Error polling real-time price: " + e.getMessage());
            }
        }, 0, 35, TimeUnit.SECONDS);
    }
}
