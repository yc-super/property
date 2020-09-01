package com.yc.property.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private int id;
    private String orderId;
    private int typeId;
    private String remarks;
    private int imgId1;
    private int imgId2;
    private int imgId3;
    private int workerImgId;
    private int asess;
    private boolean ifAsess ;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private Date modifyTime;
    private int status;

}
