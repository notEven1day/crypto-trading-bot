package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class HoldingStockRepository {

    private final JdbcTemplate jdbcTemplate;

    public HoldingStockRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> findByCoinId(Long coinId) {
        try {
            return jdbcTemplate.queryForMap(
                    "SELECT * FROM holding_stock WHERE wallet_id = 1 AND currency_id = ?",
                    coinId
            );
        } catch (Exception e) {
            return null;
        }
    }

    public void insert(Long coinId, double price, double quantity) {
        System.out.println("Inserting " + coinId + price + quantity);
        jdbcTemplate.update(
                "INSERT INTO holding_stock (wallet_id, currency_id, average_price, quantity) VALUES (1, ?, ?, ?)",
                coinId, price, quantity
        );
    }

    public void update(Long coinId, double averagePrice, double quantity) {
        System.out.println("Updating " + coinId + quantity);
        jdbcTemplate.update(
                "UPDATE holding_stock SET average_price = ?, quantity = ? WHERE wallet_id = 1 AND currency_id = ?",
                averagePrice, quantity, coinId
        );
    }
}
