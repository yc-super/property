package com.yc.property.Service;

import com.yc.property.vo.OrderVo;

import java.util.ArrayList;

public interface WorkPropertyService {
    ArrayList<OrderVo> getOrderList(String workerPhone);
    ArrayList<OrderVo> getTypeOrderList(String workerPhone,String keyWords,String values);
    int feedBack(String feedBack, String cardId);
}
