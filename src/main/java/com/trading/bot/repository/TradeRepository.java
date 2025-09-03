package com.trading.bot.repository;

import com.trading.bot.dto.TradeDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class TradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveTrade(Long coinId, double price, double quantity, String action, Double profit, String status) {
        System.out.println("Saving trade");
        System.out.println("CoinId: " + coinId);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Action: " + action);
        System.out.println("Profit: " + profit);
        System.out.println("Status: " + status);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Timestamp timestamp = Timestamp.from(now.toInstant());
        System.out.println(timestamp);
        jdbcTemplate.update(
                "INSERT INTO trades (wallet_id, currency_id, price, quantity, action, profit, status, timestamp) " +
                        "VALUES (1, ?, ?, ?, ?, ?, ?, ?)",
                coinId,
                price,
                quantity,
                action,
                profit,
                status,
                timestamp
        );
    }

    public List<TradeDto> getTrades(int page, int size) {
        String sql = """
        SELECT c.name AS currency_name, 
           t.price, 
           t.quantity, 
           t.action, 
           t.profit, 
           t.status, 
           t.timestamp
    FROM trades t
    JOIN currency c ON t.currency_id = c.id
    ORDER BY t.timestamp DESC
    LIMIT ? OFFSET ?
""";

        return jdbcTemplate.query(sql, new Object[]{size, page * size}, (rs, rowNum) -> {
            TradeDto tradeDto = new TradeDto();
            tradeDto.setCurrencyName(rs.getString("currency_name"));
            tradeDto.setPrice(rs.getDouble("price"));
            tradeDto.setQuantity(rs.getDouble("quantity"));
            tradeDto.setAction(rs.getString("action"));
            tradeDto.setProfit(rs.getObject("profit") != null ? rs.getDouble("profit") : null);
            tradeDto.setStatus(rs.getString("status"));
            tradeDto.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return tradeDto;
        });

    }
}
