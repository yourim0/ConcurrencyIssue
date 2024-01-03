package com.example.stock.service;

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

//테스트 코드 작성
@SpringBootTest
class StockServiceTest {
        @Autowired
        private StockService stockService;
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
        public void 재고감소(){
         stockService.decrease(1L, 1L);
         //100-1 =99
         Stock stock = stockRepository.findById(1L).orElseThrow();

         assertEquals(99,stock.getQuantity());
        }


        //실패 원인 : 레이스 컨디션 발생. 둘 이상의 thread가 공유데이터에 엑세스할 수 있고 동시에 변경을 하려고 할 때 발생한다.
        //해결 방안 : 데이터에 한개의 쓰레드만 접근이 가능하도록 하면 된다.
        @Test
        public void 동시에_100개의_요청() throws InterruptedException {
            int threadCount = 100;
            //멀티스레드 이용
            ExecutorService executorService = Executors.newFixedThreadPool(32);//비동기로 실행하는 작업을 단순화 하여 사용할 수 있게하는 자바 api
            CountDownLatch latch = new CountDownLatch(threadCount);//다른 스레드에서 실행되고 있는 작업이 완료될때 까지 대기할 수 있게 해주는 클래스

            for(int i = 0;i<threadCount; i++) {
                executorService.submit(() -> {
                        try {
                            stockService.decrease(1L, 1L);
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
