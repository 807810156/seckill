package com.itlicode.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.itlicode.pojo.Stock;
import com.itlicode.service.StockService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName StockController
 * @Description 请描述类的业务用途
 * @Author liweijian
 * @Date 2020-12-29 10:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("stock")
public class StockController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StockController.class);


    @Autowired
    private StockService stockService;

    /**
     * 每秒放行10个请求
     */
    RateLimiter rateLimiter = RateLimiter.create(10);


    @RequestMapping("/all")
    @ResponseBody
    public String stock(){

        List<Stock> stocks = stockService.selectAll();

        return JSON.toJSONString(stocks);
    }


    /**
     * ########################## 1.0 #######################
     */

    /**
     * 普通的获取库存方法，1000线程生成了1000左右的订单，库存只使用了36多。
     * @param sid
     * @return
     */
    @RequestMapping("/createWrongOrder/{sid}")
    @ResponseBody
    private String createWrongOrder(@PathVariable  int sid){
        LOGGER.info("购买物品编号sid=[{}]", sid);
        int id = 0;
        try {
            id = stockService.createWrongOrder(sid);
            LOGGER.info("创建订单id: [{}]", id);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return String.valueOf(id);
    }


    /**
     * ########################## 2.0 #######################
     */

    /**
     * 通过乐观锁的获取库存
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrder/{sid}")
    @ResponseBody
    private String createOptimisticOrder(@PathVariable  int sid){
        int id;
        try {
            id = stockService.createOptimisticOrder(sid);
            LOGGER.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            LOGGER.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }


    /**
     * ########################## 2.1 #######################
     */

    /**
     * 通过乐观锁+限流的获取库存
     * 使用Guava谷歌本地缓存实现接口限流,RateLimiter工具实现令牌桶限流
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrCurrentOrder/{sid}")
    @ResponseBody
    private String createOptimisticOrCurrentOrder(@PathVariable  int sid){
        //阻塞式获取令牌，请求进来后，若令牌桶里没有足够的令牌，就在这里阻塞住，等待令牌的发放。
        /**
         * 总结: 商品全部卖出；由于前面的用户接到令牌了，后面用户令牌没有，当前请求会一直等待到抢到令牌为止，
         *      这种情况会用户的体验不好，用户侧的响应时间过长。
         */
        LOGGER.info("等待时间:" + rateLimiter.acquire());
        /**
         * 非阻塞式获取令牌，请求进来后，若令牌桶里没有足够的令牌，
         * 会尝试等待设置好的时间（这里写了1000ms），其会自动判断在1000ms后，
         * 这个请求能不能拿到令牌，如果不能拿到，直接返回抢购失败。
         * 如果timeout设置为0，则等于阻塞时获取令牌
         *
         * 总结：可能会出现商品未能全部卖出。因为抢到令牌的用户正在处理，或者是乐观锁出现的并发情况，会导致
         *      部分用户是抢购到了商品，返回了乐观锁版本不一致的情况；也出了令牌不足的情况，商品未能全部卖出。
         */
//        if (rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
//            LOGGER.warn("你被限流了，真不幸，直接返回失败");
//            return "购买失败，库存不足";
//        }

        int id;
        try {
            id = stockService.createOptimisticOrder(sid);
            LOGGER.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            LOGGER.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }


    /**
     * ########################## 3.0 #######################
     */
}
