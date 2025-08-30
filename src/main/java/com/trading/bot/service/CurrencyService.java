package com.trading.bot.service;

import com.trading.bot.repository.CurrencyRepository;
import com.trading.bot.model.Currency;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private final CoinGeckoService coinGeckoService;
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CoinGeckoService coinGeckoService, CurrencyRepository currencyRepository) {
        this.coinGeckoService = coinGeckoService;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Fetch all currencies from CoinGecko and store them in the DB.
     * Only inserts new symbols, avoids duplicates.
     */
    public void fetchAndStoreAllCurrencies() {
        List<Map<String, String>> coins = coinGeckoService.fetchAllCoins();

        if (coins != null) {
            for (Map<String, String> coin : coins) {
                String symbol = coin.get("symbol").toUpperCase();
                String coinId = coin.get("id");       // this is unique, use as primary lookup
                String name = coin.get("name");       // store for readability

                // insert only if coingecko_id doesn't exist
                currencyRepository.insertIfNotExists(symbol, coinId, name);
            }
        }
    }
}
