package com.yc.property.Service;

import com.yc.property.Bean.*;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import com.yc.property.vo.WorkerVo;

import java.util.ArrayList;

public interface AdminService {

    Admin adminLogin(Admin admin);

    int getOwnerCount(String name,String phone,String buildingId);

    ArrayList<OwnerVo> getPagingOwnerList(int currentPage,int pageSize,String name,String phone,String buildingId);

    ArrayList<Integer> getAllBuilding();

    int addOwner(OwnerVo ownerVo);

    int deleteOwners(String[] phones);

    int updateOwners(OwnerVo ownerVo);

    //以下关于Worker
    int getWorkerCount(String name,String phone);

    ArrayList<WorkerVo> getPagingWorkerList(int currentPage, int pageSize, String name, String phone);

    int addWorker(WorkerVo workerVo);

    int deleteWorkers(String[] phones);

    int updateWorkers(WorkerVo workerVo);

    //以下关于订单
    ArrayList<String> getAllWorkerName();

    int getOrderCount(String orderId,String ownerName,String workerName,int status);

    ArrayList<OrderVo> getPagingOrderList(int currentPage, int pageSize, String orderId,String ownerName,String workerName,int status);

    int deleteOrders(String[] phones);

    int updateOrder(OrderVo orderVo);




    /////////////////////////以下估计用不到
    int backFeedBackByWorkId(int id, String backValue);

    ArrayList<feedBack> getAllFeedBack();
}
