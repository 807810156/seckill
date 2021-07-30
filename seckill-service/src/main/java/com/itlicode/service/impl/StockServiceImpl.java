package com.itlicode.service.impl;

import com.itlicode.mapper.StockMapper;
import com.itlicode.mapper.StockOrderMapper;
import com.itlicode.pojo.Stock;
import com.itlicode.pojo.StockOrder;
import com.itlicode.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName StockServiceImpl
 * @Description 库存业务层
 * @Author liweijian
 * @Date 2020-12-29 10:59
 * @Version 1.0
 **/
@Service("stockService")
public class StockServiceImpl implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockOrderMapper stockOrderMapper;

    /**
     * 获取当前所有库存数据
     * @return Result<Stock>
     */
    @Override
    public List<Stock> selectAll() {
        return stockMapper.selectAll();
    }

    @Override
    @ProcessAnnotation(content = "111", title = "222", orderNoPlace = 0, type = 0)
    @Transactional(rollbackFor = Exception.class)
    public int createWrongOrder(int sid) {

        //校验库存
        Stock stock = checkStock(sid);

        //更新已销售库存
        updateSaleStock(stock);

        //创建订单
        int id = createOrder(stock);

        return id;
    }

    /**
     * 通过乐观锁防止秒杀超卖
     * @param sid
     * @return
     */
    @Override
    public int createOptimisticOrder(int sid) {

        //校验库存
        Stock stock = checkStock(sid);
        //通过乐观锁更新库存
        updateOptimisticSaleStock(stock);

        //创建订单
        int id = createOrder(stock);

        return stock.getCount() - (stock.getSale()+1);
    }

    @Override
    public int createOptimisticOrzCurrentOrder(int sid) {
        return 0;
    }

    /**
     * 通过乐观锁更新库存
     * @param sid
     * @return
     */
    private void updateOptimisticSaleStock(Stock stock) {
        LOGGER.info("查询数据库，尝试更新库存");

        int i = stockMapper.updateByOptimistic(stock);

        if(0 == i){
            throw new RuntimeException("并发更新库存失败，version不匹配");
        }
    }

    /**
     * 更新库存
     * @param stock
     */
    private void updateSaleStock(Stock stock) {
        stock.setSale(stock.getSale()+1);
        stockMapper.updateByPrimaryKey(stock);
    }

    /**
     * 校验库存
     * @param sid
     * @return
     */
    private Stock checkStock(int sid) {

        Stock stock = stockMapper.selectByPrimaryKey(sid);

        if(stock.getSale().equals(stock.getCount())){
            throw new RuntimeException("库存不足");
        }

        return stock;
    }

    /**
     * 创建库存工单
     * @param stock
     * @return
     */
    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        int id = stockOrderMapper.insert(order);
        return id;
    }
}
