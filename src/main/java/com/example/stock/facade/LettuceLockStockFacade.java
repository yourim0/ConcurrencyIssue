package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private RedisLockRepository redisLockRepository;
    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)){ //lock 획득 시도
            Thread.sleep(100);//lock 획득 실패 시
        }
        try{
            stockService.decrease(id, quantity); //락 획득 성공 시 재고감소
        } finally{
            redisLockRepository.unlock(id); //로직 종료 후 락 해제
        }
    }
}
