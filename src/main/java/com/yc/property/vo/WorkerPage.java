package com.yc.property.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPage {
    private ArrayList<WorkerVo> workerVos;

    private int currentPage;//目前页数
    private int pageSize;//页面大小，（每页有几条数据）
    private int pageCount;//一共有多少页数
    private int count;//总条数

    public WorkerPage(ArrayList<WorkerVo> workerVos, int count){
        this.workerVos=workerVos;
        this.count=count;
    }
}
