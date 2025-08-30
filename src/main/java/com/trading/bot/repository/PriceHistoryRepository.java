package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class PriceHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public PriceHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert price entry, avoid duplicates
    public void insertPrice(Long coinId, double price, LocalDateTime timestamp) {
        System.out.println("Inserting price history for coinId: " + coinId + " price: " + price + " timestamp: " + timestamp);
        String sql = "INSERT INTO price_history (currency_id, price, timestamp) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, coinId, price, java.sql.Timestamp.valueOf(timestamp));
    }

}
