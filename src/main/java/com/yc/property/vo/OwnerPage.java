package com.yc.property.vo;

import com.yc.property.Bean.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerPage {
    private ArrayList<OwnerVo> ownerVos;

    private int currentPage;//目前页数
    private int pageSize;//页面大小，（每页有几条数据）
    private int pageCount;//一共有多少页数
    private int count;//总条数

    public OwnerPage(ArrayList<OwnerVo> ownerVos,int count){
        this.ownerVos=ownerVos;
        this.count=count;
    }
}
