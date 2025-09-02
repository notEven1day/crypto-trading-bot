package com.trading.bot.service;

import com.trading.bot.config.CurrentTradingTime;
import com.trading.bot.dto.TradeDto;
import com.trading.bot.enums.TradingDecision;
import com.trading.bot.repository.TradeRepository;
import com.trading.bot.strategy.MovingAverageCrossoverStrategy;
import com.trading.bot.config.BotState;
import com.trading.bot.service.HoldingStockService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TradingService {

    private final MovingAverageCrossoverStrategy maStrategy;
    private final HoldingStockService holdingStockService;
    private CurrentTradingTime currentTradingTime;
    private final BotState botState; // has boolean isTrading
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> tradingTask;
    private final TradeRepository tradeRepo;


    public TradingService(MovingAverageCrossoverStrategy maStrategy,
                          HoldingStockService holdingStockService,
                          BotState botState, CurrentTradingTime currentTradingTime, TradeRepository tradeRepo) {
        this.maStrategy = maStrategy;
        this.holdingStockService = holdingStockService;
        this.botState = botState;
        this.currentTradingTime = currentTradingTime;
        this.tradeRepo = tradeRepo;
    }
    public void startTrading(Long coinId) {
        if (botState.isTrading()) return; // already running

        botState.startTrading();
        tradingTask = scheduler.scheduleAtFixedRate(() -> {
                    // 1️⃣ Update current trading time
                    if (currentTradingTime.isTrainingMode()) {
                        currentTradingTime.advanceTimeBySeconds(24 * 60 * 60); // +1 day
                    } else {
                        currentTradingTime.advanceTimeBySeconds(35); // +35 sec
                    }

                    // 2️⃣ Stop trading if we've reached yesterday (for training/backtesting)
                    if (currentTradingTime.isTrainingMode()) {
                        if (currentTradingTime.getCurrentTime().isAfter(LocalDateTime.now().minusDays(1))) {
                            System.out.println("Reached end of training period. Stopping bot.");
                            stopTrading();
                            return;
                        }
                    }

                    // 3️⃣ Evaluate trading
                evaluate(coinId);
                },
                0, 35, TimeUnit.SECONDS);
    }

    public void stopTrading() {
        botState.stopTrading();
        if (tradingTask != null) {
            tradingTask.cancel(false);
        }
    }
    /**
     * Evaluate trading decision but only execute if bot is active
     */
    public void evaluate(Long coinId) {
        if (!botState.isTrading()) return;

        TradingDecision decision = maStrategy.evaluate(coinId,currentTradingTime.getCurrentTime()); // BUY, SELL, HOLD
        System.out.println(decision.toString());
        switch (decision) {
            case BUY -> holdingStockService.buy(coinId);
            case SELL -> holdingStockService.sell(coinId);
            case HOLD -> {} // do nothing
        }
    }

    public List<TradeDto> getTrades(int page, int size) {
        return tradeRepo.getTrades(page, size);
    }

}
