package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyRepository {

    private final JdbcTemplate jdbcTemplate;

    public CurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new currency only if symbol doesn't exist
     */
    public void insertIfNotExists(String symbol, String coinId) {

        String sql = "INSERT INTO currency (symbol, coingecko_id) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (symbol) DO NOTHING";

        if(symbol.length() <= 50 && coinId.length() <= 50) {
            jdbcTemplate.update(sql, symbol, coinId);
        } else {
            // skip this currency
            // might lose some test cryptos, but worth the loss
        }


    }
}
