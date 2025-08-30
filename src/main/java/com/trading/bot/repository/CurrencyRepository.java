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
    public void insertIfNotExists(String symbol, String coinId, String name) {
        String sql = "INSERT INTO currency (symbol, coingecko_id, name) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (coingecko_id) DO NOTHING";

        if (symbol.length() <= 50 && coinId.length() <= 50 && name.length() <= 50) {
            jdbcTemplate.update(sql, symbol, coinId, name);
        } else {
            // skip this currency
            // avoids errors if symbol, id, or name is too long
        }
    }

    public String getCoinGeckoIdByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT coingecko_id FROM currency WHERE name = ?",
                new Object[]{name},
                String.class
        );
    }

    public Long getCoinIdByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM currency WHERE name = ?",
                new Object[]{name},
                Long.class
        );
    }





}

