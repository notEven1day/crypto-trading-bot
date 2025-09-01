package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    public Long getCoinIdByCoingeckoId(String coingeckoId) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM currency WHERE coingecko_id = ?",
                new Object[]{coingeckoId},
                Long.class
        );
    }




    public List<Map<String, Object>> findAllCurrencies() {
        return jdbcTemplate.queryForList(
                "SELECT name, symbol, coingecko_id FROM currency ORDER BY name ASC"
        );
    }





}

