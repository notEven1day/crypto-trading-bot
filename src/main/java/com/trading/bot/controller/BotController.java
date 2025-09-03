package com.trading.bot.controller;

import com.trading.bot.dto.ModeRequest;
import com.trading.bot.service.BotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private final BotService botService;
    public BotController(BotService botService) {
        this.botService=botService;
    }
    @PostMapping("/setMode")
    public String setMode(@RequestBody ModeRequest request) {
        botService.setMode(request.getMode());

        return "Mode set to " + request.getMode()
                + ". Current time: " + botService.getCurrentTradingTime();
    }

    @GetMapping ("/status")
    public boolean botStatus() {
        return botService.isBotTrading();
    }
}
