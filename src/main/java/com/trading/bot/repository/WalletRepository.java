package com.trading.bot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepository {
    private final JdbcTemplate jdbcTemplate;

    public WalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean walletExists(Long walletId) {
        String sql = "SELECT COUNT(*) FROM wallet WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, walletId);
        return count != null && count > 0;
    }

    public void updateBalance(Long walletId, double amount) {
        String sql = "UPDATE wallet SET capital = capital + ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, walletId);
    }

    public void updateBalanceBuy(Long walletId, double amount) {
        String sql = "UPDATE wallet SET capital = capital - ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, walletId);
    }

    public void createWallet(Long walletId, double amount) {
        String sql = "INSERT INTO wallet (id, capital) VALUES (?, ?)";
        jdbcTemplate.update(sql, walletId, amount);
    }

    public Double getBalance(Long walletId) {
        String sql = "SELECT capital FROM wallet WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, walletId);
    }

}
