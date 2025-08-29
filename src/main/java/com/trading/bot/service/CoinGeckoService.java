package com.trading.bot.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CoinGeckoService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getBitcoinLastYear() {
        String url = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=365";
        return restTemplate.getForObject(url, Map.class);
    }
}
