package com.example.stock.service;


import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    //재고 감소 메서드
    //방법1. synchronized 사용. 해당 메서드는 한 개의 스레드만 접근이 가능하게 된다.
    //@Transactional //error : Transactional 어노테이션을 적용하면 만든 클래스를 래핑한 클래스를 새로 만들어서 실행하게 된다.
    public synchronized void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findById(id).orElseThrow(); //조회 / 내가예외처리 하겠다.
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

}
