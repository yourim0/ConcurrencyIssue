package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade { //optimistic-lock은 실패했을 때 재시도하기 위함.
    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        //update 실패했을 때 재시도 해야하므로
        while(true){
            try{
                optimisticLockStockService.decrease(id, quantity);
                break; //정상적 업데이트 시
            } catch(Exception e){
                Thread.sleep(50);//50millisecond후 재시도
            }
        }

    }
}
