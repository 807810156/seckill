package com.itlicode.mapper;

import com.itlicode.base.BaseMapper;
import com.itlicode.pojo.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    int updateByOptimistic(Stock stock);
}