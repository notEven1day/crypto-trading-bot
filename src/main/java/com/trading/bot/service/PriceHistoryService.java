package com.trading.bot.service;

import com.trading.bot.config.CurrentTradingTime;
import com.trading.bot.config.CurrentlyTrading;
import com.trading.bot.repository.CurrencyRepository;
import com.trading.bot.repository.PriceHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
    private final RestTemplate restTemplate = new RestTemplate();

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
        String coinGeckoId = currentlyTrading.getCoinGeckoId();
        Long coinId = currencyRepository.getCoinIdByCoingeckoId(coinGeckoId);
        startRealTimePolling(coinId, coinGeckoId);

    }

    // Training / Backtesting
    //TODO: Might need to revert
//    private void fetchHistoricalForTraining(Long coinId, String coinGeckoId) {
//        LocalDateTime fromTime = currentTradingTime.getCurrentTime();
//        LocalDateTime now = LocalDateTime.now();
//
//        Map<String, Object> response = coinGeckoService.getPriceHistory(coinGeckoId, fromTime, now);
//        List<List<Object>> prices = (List<List<Object>>) response.get("prices");
//
//        if (prices != null) {
//            for (List<Object> entry : prices) {
//                long timestampMs = ((Number) entry.get(0)).longValue();
//                double price = ((Number) entry.get(1)).doubleValue();
//                LocalDateTime ts = Instant.ofEpochMilli(timestampMs)
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();
//
//                priceHistoryRepository.insertPrice(coinId, price, ts);
//            }
//        }
//
//        // Move simulated clock forward
//        currentTradingTime.setCurrentTime(now);
//    }

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

    public void backfillYesterday() {
        String coingeckoId = currentlyTrading.getCoinGeckoId();
        Long coinId = currencyRepository.getCoinIdByCoingeckoId(coingeckoId);

        long now = Instant.now().getEpochSecond();
        long oneDayAgo = now - (24L * 60 * 60); // 24 hours ago

        String url = "https://api.coingecko.com/api/v3/coins/" + coingeckoId +
                "/market_chart/range?vs_currency=usd&from=" + oneDayAgo + "&to=" + now;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("prices")) return;

        List<List<Object>> prices = (List<List<Object>>) response.get("prices");

        for (List<Object> entry : prices) {
            long timestampMs = ((Number) entry.get(0)).longValue();
            double price = ((Number) entry.get(1)).doubleValue();

            LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneOffset.UTC);

            priceHistoryRepository.insertPrice(coinId, price, time);
        }
    }

}
