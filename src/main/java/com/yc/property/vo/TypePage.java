package com.yc.property.vo;

import com.yc.property.Bean.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypePage {
    private ArrayList<Type> types;

    private int currentPage;//目前页数
    private int pageSize;//页面大小，（每页有几条数据）
    private int pageCount;//一共有多少页数
    private int count;//总条数

    public TypePage(ArrayList<Type> types, int count){
        this.types=types;
        this.count=count;
    }
}
