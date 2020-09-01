package com.yc.property.Service;

import com.yc.property.Bean.Type;
import com.yc.property.Bean.Worker;
import com.yc.property.vo.OrderVo;

import java.util.ArrayList;
import java.util.Date;

public interface ApplyPropertyService {
    ArrayList<Type> getAllType();
    ArrayList<Worker> getAllWorker();
    int getWorkerUnfinishedOrderNum(String workerId);
    int getOwnerUnfinishedOrderNum(String ownerPhone);
    ArrayList<OrderVo> getTypeOrderList(String workerPhone,String keyWords,String values);
    int createOrder(String ownerPhone,String workerPhone);
    String getMaxOrderId(String ownerPhone);
    int createOrderDetail(OrderVo orderVo);
    OrderVo getOrderDetailByOrderId(String orderId);
    int startProperty(String orderId, Date date);
    int endProperty(String orderId,Date date);
    int createAsess(OrderVo orderVo);
    OrderVo getNewestPropertyOrder(String ownerPhone);
    int cancelProperty(String orderId);
    int getApplyNumByOwnerPhone(String ownerPhone);
    int getMaxPriorityByWorkerPhone(String workerPhone);
    int setMaxPriorityByOrderId(String orderId,int maxPriority);
    int setPingjiaByOrderId(String orderId,int asess);
    String getCalTimeByWorkerPhone(String workerPhone);
    String getAvgAsessByWorkerPhone(String workerPhone);
    int savePicture(String id,String path);
}
