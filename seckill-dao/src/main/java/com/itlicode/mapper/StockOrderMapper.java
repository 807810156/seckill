package com.itlicode.mapper;

import com.itlicode.pojo.StockOrder;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface StockOrderMapper extends BaseMapper<StockOrder> {
}