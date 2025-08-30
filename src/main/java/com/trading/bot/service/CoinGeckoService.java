package com.trading.bot.service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CoinGeckoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "CG-eGF8VvPLHJUF7SfYHE9J5EkV";

    public Map<String, Object> getBitcoinLastYear() {
        String url = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=365";
        return restTemplate.getForObject(url, Map.class);
    }
    public List<Map<String, String>> fetchAllCoins() {
        String url = "https://api.coingecko.com/api/v3/coins/list";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-pro-api-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        return response.getBody();
    }
}
