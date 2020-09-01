package com.yc.property.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPage {
    private ArrayList<OrderVo> orderVos;

    private int currentPage;//目前页数
    private int pageSize;//页面大小，（每页有几条数据）
    private int pageCount;//一共有多少页数
    private int count;//总条数

    public OrderPage(ArrayList<OrderVo> orderVos, int count){
        this.orderVos=orderVos;
        this.count=count;
    }
}
