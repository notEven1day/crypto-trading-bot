package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<Double> findLatestPrices(Long coinId, int limit) {
        String sql = "SELECT price FROM price_history " +
                "WHERE currency_id = ? " +
                "ORDER BY timestamp ASC " +
                "LIMIT ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{coinId, limit},
                (rs, rowNum) -> rs.getDouble("price")
        );
    }

    public Double calculateSMA(Long coinId, int period, LocalDateTime tradingTime) {
        String sql = "SELECT price " +
                "FROM price_history " +
                "WHERE currency_id = ? AND timestamp <= ? " +
                "ORDER BY timestamp DESC " +
                "LIMIT ?";

        List<Double> prices = jdbcTemplate.queryForList(
                sql,
                Double.class,
                coinId,
                tradingTime,
                period
        );

        if (prices.size() < period) {
            return null; // not enough data yet
        }

        return prices.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }


//    public Double findLatestPrice(Long coinId) {
//        String sql = """
//            SELECT price
//            FROM price_history
//            WHERE currency_id = ?
//            ORDER BY timestamp DESC
//            LIMIT 1
//        """;
//
//        return jdbcTemplate.query(sql, rs -> {
//            if (rs.next()) {
//                return rs.getDouble("price");
//            }
//            return null;
//        }, coinId);
//    }

    public Double findLatestPrice(Long coinId, LocalDateTime currentTradingTime) {
        String sql = """
        SELECT price
        FROM price_history
        WHERE currency_id = ?
          AND timestamp < ?
        ORDER BY timestamp DESC
        LIMIT 1
    """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return rs.getDouble("price");
            }
            return null;
        }, coinId, Timestamp.valueOf(currentTradingTime));
    }


}
