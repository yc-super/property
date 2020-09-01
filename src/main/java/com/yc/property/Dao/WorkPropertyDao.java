package com.yc.property.Dao;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Mapper
public interface WorkPropertyDao {
    ArrayList<OrderVo> getOrderList(String workerPhone);
    ArrayList<OrderVo> getUnfinishedOrderByWorkerPhone(String workerPhone);
    ArrayList<OrderVo> getFinishedOrderByWorkerPhone(String workerPhone);
    ArrayList<OrderVo> getCancelOrderByWorkerPhone(String workerPhone);
    ArrayList<OrderVo> getOrderDetailByOrderId(String orderId);
    ArrayList<OrderVo> getOrderByBuildingIdAndWorkerPhone(int buildingId,String workerPhone);
    ArrayList<OrderVo> getOrderByOwnerNameAndWorkerPhone(String ownerName,String workerPhone);
    //根据orderId查询该订单是否存在
    OrderVo isOrderExistByOrderIdAndWorkerPhone(String orderId,String workerPhone);
    Worker getWorkerByOrderId(String orderId);
    Owner getOwnerByOrderId (String orderId);
    int getStatusByOrderId(String orderId);
    String getTypeNameByTypeId(int typeId);
    int feedBack(String feedBack, String cardId);
}
