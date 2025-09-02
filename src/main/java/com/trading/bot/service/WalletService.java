package com.trading.bot.service;

import com.trading.bot.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void loadFunds(Long walletId, double amount) {
        if (walletRepository.walletExists(walletId)) {
            walletRepository.updateBalance(walletId, amount);
        } else {
            walletRepository.createWallet(walletId, amount);
        }
    }

    public void updateFunds(Long walletId, double amount, String status) {
        if (status.equals("BUY")) {
            walletRepository.updateBalanceBuy(walletId,amount);
        }
        else {
            walletRepository.updateBalance(walletId, amount);
        }
    }

    //TODO: wallet id only 1
    public Double getBalance() {
        return walletRepository.getBalance(1L); // always wallet id 1
    }
}
