package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity){
        try{
            lockRepository.getLock(id.toString()); //락 획득
            stockService.decrease(id, quantity);
        } finally {
            //모든로직 종료 후
            lockRepository.releaseLock(id.toString()); //락 헤제
        }
    }
}
