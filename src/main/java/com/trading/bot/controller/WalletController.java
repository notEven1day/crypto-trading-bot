package com.trading.bot.controller;

import com.trading.bot.dto.LoadFundsRequest;
import com.trading.bot.service.WalletService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/load-funds")
    public String loadFunds(@RequestBody LoadFundsRequest request) {
        walletService.loadFunds(request.getWalletId(), request.getAmount());
        return "Funds loaded successfully into wallet " + request.getWalletId();
    }
    @GetMapping("/balance")
    public Double getBalance() {
        return walletService.getBalance();
    }
}
