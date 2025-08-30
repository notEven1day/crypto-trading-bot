package com.trading.bot.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
public class CoinGeckoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "CG-eGF8VvPLHJUF7SfYHE9J5EkV"; // TODO: move to application.properties
    private final String API_BASE = "https://api.coingecko.com/api/v3";

    private HttpEntity<String> buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-pro-api-key", apiKey);
        return new HttpEntity<>(headers);
    }

    // Fetch all available coins: id, symbol, name
    public List<Map<String, String>> fetchAllCoins() {
        String url = API_BASE + "/coins/list";

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHeaders(),
                List.class
        );

        return response.getBody();
    }

    /**
     * Get historical price data (training mode)
     * @param coinId Coingecko ID of the coin
     * @param from start time
     * @param to end time
     */
    public Map<String, Object> getPriceHistory(String coinId, LocalDateTime from, LocalDateTime to) {
        long fromTs = from.toEpochSecond(ZoneOffset.UTC);
        long toTs = to.toEpochSecond(ZoneOffset.UTC);

        String url = String.format(
                "%s/coins/%s/market_chart/range?vs_currency=usd&from=%d&to=%d",
                API_BASE, coinId.toLowerCase(), fromTs, toTs
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHeaders(),
                Map.class
        );

        return response.getBody();
    }

    /**
     * Get the latest price (live mode)
     * @param coinId Coingecko ID of the coin
     */
    public Map<String, Object> getLatestPrice(String coinId) {
        String url = String.format(
                "%s/simple/price?ids=%s&vs_currencies=usd",
                API_BASE, coinId.toLowerCase()
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                buildHeaders(),
                Map.class
        );

        return response.getBody();
    }
}
