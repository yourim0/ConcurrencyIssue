package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NamedLockStockFacadeTest {

    @Autowired
    private NamedLockStockFacade namedLockStockFacade;
    //optimisticLock 실제로 락을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법.

    @Autowired
    private StockRepository stockRepository;
    @BeforeEach //데이터 생성
    public void before(){
        stockRepository.saveAndFlush(new Stock(1L,100L));
    }
    //종료되면 모든 아이템을 삭제
    @AfterEach
    public void after(){
        stockRepository.deleteAll();//모든 아이템을 삭제
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        //멀티스레드 이용
        ExecutorService executorService = Executors.newFixedThreadPool(32);//비동기로 실행하는 작업을 단순화 하여 사용할 수 있게하는 자바 api
        CountDownLatch latch = new CountDownLatch(threadCount);//다른 스레드에서 실행되고 있는 작업이 완료될때 까지 대기할 수 있게 해주는 클래스

        for(int i = 0;i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    namedLockStockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        //100-100 = 0;
        assertEquals(0, stock.getQuantity());

    }
}