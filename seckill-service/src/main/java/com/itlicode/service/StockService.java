package com.itlicode.service;

import com.itlicode.pojo.Stock;

import java.util.List;

public interface StockService {

    /**
     * 获取当前所有库存数据
     * @return Result<Stock>
     */
    List<Stock> selectAll();

    /**
     * 创建工单
     * @param sid
     * @return
     */
    int createWrongOrder(int sid);

    /**
     * 通过乐观锁创建工单
     * @param sid
     * @return
     */
    int createOptimisticOrder(int sid);

    /**
     * 通过乐观锁+限流
     * @param sid
     * @return
     */
    int createOptimisticOrzCurrentOrder(int sid);
}
