package com.yc.property.vo;

import com.yc.property.Bean.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    private int id;//order_detail表的id
    private String orderId;
    private String ownerId;
    private String ownerName;
    private int buildingId;
    private int address;
    private int typeId;
    private String typeName;
    private String remarks;
    private String workerId;//worker的phone
    private String workerName;
    private String imgId;
    private String imgPath;
    private int asess;
    private boolean ifAsess;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private int queueNum;
    private int status;//该订单的状态，0：待维修，1：已维修，-1：用户取消维修
    private String waitTime;//如果有排队人数，则有该预计时间，不同类型的维修类别预计时间不同，waitTime=特定维修类别预计时间*排队人数，例如：2小时35分钟
//    public OrderVo(Order order){
//        this.orderId=order.getId();
//        this.ownerId=order.getOwnerId();
//        this.workerId=order.getWorkerId();
//        this.status=order.getStatus()==1?"已完成":(order.getStatus()==0?"待维修":"用户已取消");
//    }
    public boolean getIfAsess(){
        return this.ifAsess;
    }
    public void setIfAsess(boolean ifAsess){this.ifAsess=ifAsess;}
    public void setAsess(int asess){this.asess=asess;}
    public int getAsess(){return this.asess;}
}
