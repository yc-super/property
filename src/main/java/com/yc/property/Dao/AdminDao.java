package com.yc.property.Dao;

import com.yc.property.Bean.*;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import com.yc.property.vo.WorkerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Mapper
public interface AdminDao {
    Admin adminLogin(Admin admin);

    int getOwnerCount(String name,String phone,String buildingId);

    ArrayList<Owner> getPagingOwnerList(int nowPage,int pageSize,String name,String phone,String buildingId);

    ArrayList<Integer> getAllBuilding();

    int addOwner(OwnerVo ownerVo,int status);

    int deleteOwners(String[] phones);

    int updateOwners(String phone,String sex,String password,int roomId,int status);



    int getWorkerCount(String name, String phone);

    ArrayList<Worker> getPagingWorkerList(int nowPage,int pageSize,String name,String phone);

    int addWorker(WorkerVo workerVo, int status);

    int deleteWorkers(String[] phones);

    int updateWorkers(String phone,String sex,String password,int status);

    //以下关于订单
    //获取所有维修工姓名，筛选用
    ArrayList<String> getAllWorkerName();

    int getOrderCount(String orderId,String ownerName,String workerName,int status);

    ArrayList<OrderVo> getPagingOrderList(int nowPage, int pageSize, String orderId, String ownerName, String workerName, int status);

    int deleteOrders(String[] orderIds);

    ArrayList<HashMap<Integer,String>> getAllType();
    //更改维修类别，详细描述，评分
    int updateOrder1(OrderVo orderVo);
    //更改状态
    int updateOrder2(OrderVo orderVo);
    //查询总条数
    int getTypeCount();
    ArrayList<Type> getPagingTypeList(int nowPage, int pageSize);
    int deleteTypes(int[] typeIds);
    Admin getAdminByPwd(String pwd);
    int updatePwd(String pwd);


    //下面关于type
    String isTypeExits(String typeName);
    int addType(String typeName);



    ////////////////////////

    ArrayList<feedBack> getAllFeedBack();

    int backFeedBackByWorkId(int id, String backValue);
}
