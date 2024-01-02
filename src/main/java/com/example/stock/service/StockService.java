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
    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findById(id).orElseThrow(); //조회 / 내가예외처리 하겠다.
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

}
