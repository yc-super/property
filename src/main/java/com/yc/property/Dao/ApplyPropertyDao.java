package com.yc.property.Dao;

import com.yc.property.Bean.Order;
import com.yc.property.Bean.Type;
import com.yc.property.Bean.Worker;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.TypeAndNum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface ApplyPropertyDao {
    ArrayList<Type> getAllType();
    ArrayList<Worker> getAllWorker();
    int getWorkerUnfinishedOrderNum(String workerPhone);
    int getOwnerUnfinishedOrderNum(String ownerPhone);
    int createOrder(String orderId,String ownerPhone,String workerPhone);
//    String getMaxOrderId(String ownerPhone);
    OrderVo getOrderDetailByOrderId2(String orderId);
    Order getOrderByOrderId(String orderId);
    int createOrderDetail(OrderVo orderVo);
    int startProperty(String orderId, Date date);
    int endProperty(String orderId,Date date);
    int createAsess(OrderVo orderVo);
    String getTypenameByTypeid(int typeId);
    String getNewestPropertyOrderId(String ownerPhone);
    //获取排队人数
    int getQueueNum(String workerPhone,String orderId);
    //查询该订单是否已经开始
    Order getOrderByOrderIdAndStartTime(String orderId);
    List<TypeAndNum> getUnfinishedTypes(String workerPhone, String orderId);
    //获取预计时间，无orderId,与现在时间作比较
    List<TypeAndNum> getUnfinishedTypes2(String workerPhone);

    int setStatus(String orderId,int status);

    ArrayList<OrderVo> getUnfinishedOrderByOwnerPhone(String ownerPhone);
    ArrayList<OrderVo> getFinishedOrderByOwnerPhone(String ownerPhone);
    ArrayList<OrderVo> getCancelOrderByOwnerPhone(String ownerPhone);
    ArrayList<OrderVo> getOrderByOwnerPhoneAndWorkerPhone(String ownerPhone,String workerName);
    //根据orderId查询该订单是否存在
    OrderVo isOrderExistByOrderIdAndOwnerPhone(String orderId,String ownerPhone);

    int getApplyNumByOwnerPhone(String ownerPhone);

    //根据typeId获得该维修类别所有已完成订单结束时间-开始时间，返回一个list
    ArrayList<Integer> getTimesByTypeId(int typeId,String workerPhone);

    //获取某维修工待维修订单中最大的优先级
    int getMaxPriorityByWorkerPhone(String workerPhone);

    int setMaxPriorityByOrderId(String orderId,int maxPriority);

    //根据orderId设置评价评分，共5分
    int setPingjiaByOrderId(String orderId,int asess);

    //获取某维修工预计时间，用于申请时用
    String getCalTimeByWorkerPhone(String workerPhone);

    //获取某维修工平均平分，用于申请时用
    String getAvgAsessByWorkerPhone(String workerPhone);

    //获取最大订单号
    String getMaxOrderId();


    int getImgNumByImgId(String id);
    int savePicture(String id,String path);
    int updateImg(String id,String path);

    //创建订单时设置图片id
    int setImgIdByOrderId(String imgId,String orderId);

    String getImgPathByImgId(String imgId);

    ArrayList<OrderVo> getNoAsessOrderByOwnerPhone(String ownerPhone);

}
