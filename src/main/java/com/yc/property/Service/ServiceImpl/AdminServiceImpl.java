package com.yc.property.Service.ServiceImpl;

import com.yc.property.Bean.*;
import com.yc.property.Dao.AdminDao;
import com.yc.property.Dao.ApplyPropertyDao;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Dao.WorkerDao;
import com.yc.property.Service.AdminService;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import com.yc.property.vo.WorkerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private WorkerDao workerDao;
    @Autowired
    private ApplyPropertyDao applyPropertyDao;
    @Override
    public Admin adminLogin(Admin admin) {
        return adminDao.adminLogin(admin);
    }

    @Override
    public int getOwnerCount(String name,String phone,String buildingId){
        if(StringUtils.isEmpty(buildingId)){
            return adminDao.getOwnerCount(name,phone,null);
        }else{
            return adminDao.getOwnerCount(name,phone,buildingId);
        }
    }

    @Override
    public ArrayList<OwnerVo> getPagingOwnerList(int currentPage,int pageSize,String name,String phone,String buildingId){
        ArrayList<OwnerVo> ownerVos =new ArrayList<>();
        ArrayList<Owner> owners =new ArrayList<>();
        if(StringUtils.isEmpty(buildingId)){
            owners=adminDao.getPagingOwnerList((currentPage-1)*pageSize,pageSize,name,phone,null);
        }else{
            owners=adminDao.getPagingOwnerList((currentPage-1)*pageSize,pageSize,name,phone,buildingId);
        }
            for(Owner owner:owners){
                OwnerVo ownerVo1=ownerDao.getValueByRoomId(owner.getRoomId());
                OwnerVo ownerVo2=new OwnerVo(owner,ownerVo1);
                ownerVos.add(ownerVo2);
            }
        System.out.println("impl!!,ownerVos:");
        System.out.println(ownerVos);
        return ownerVos;
    }

    @Override
    public ArrayList<Integer> getAllBuilding(){
        return adminDao.getAllBuilding();
    }



    @Override
    public int addOwner(OwnerVo ownerVo) {
        int roomId=ownerDao.getRoomIdByBuildingIdAndAddress(ownerVo.getBuildingId(),ownerVo.getAddress());
        ownerVo.setRoomId(roomId);
        return adminDao.addOwner(ownerVo,Integer.parseInt(ownerVo.getStatus()));
    }



    @Override
    public int deleteOwners(String[] phones) {
        return adminDao.deleteOwners(phones);
    }

    @Override
    public int updateOwners(OwnerVo ownerVo) {
        ownerVo.setRoomId(ownerDao.getRoomIdByBuildingIdAndAddress(ownerVo.getBuildingId(),ownerVo.getAddress()));
        return adminDao.updateOwners(ownerVo.getPhone(),ownerVo.getSex(),ownerVo.getPassword(),ownerVo.getRoomId(),Integer.parseInt(ownerVo.getStatus()));
    }

    @Override
    public int getWorkerCount(String name, String phone) {
        return adminDao.getWorkerCount(name,phone);
    }

    @Override
    public ArrayList<WorkerVo> getPagingWorkerList(int currentPage, int pageSize, String name, String phone) {
        ArrayList<WorkerVo> workerVos =new ArrayList<>();
        ArrayList<Worker> workers =new ArrayList<>();
        workers=adminDao.getPagingWorkerList((currentPage-1)*pageSize,pageSize,name,phone);
        for(Worker worker:workers){
            WorkerVo workerVo2=new WorkerVo(worker);
            workerVos.add(workerVo2);
        }
        System.out.println("impl!!,workerVos:");
        System.out.println(workerVos);
        return workerVos;
    }

    @Override
    public int addWorker(WorkerVo workerVo) {
        return adminDao.addWorker(workerVo,Integer.parseInt(workerVo.getStatus()));
    }

    @Override
    public int deleteWorkers(String[] phones) {
        return adminDao.deleteWorkers(phones);
    }

    @Override
    public int updateWorkers(WorkerVo workerVo) {
        return adminDao.updateWorkers(workerVo.getPhone(),workerVo.getSex(),workerVo.getPassword(),Integer.parseInt(workerVo.getStatus()));
    }
    @Override
    public ArrayList<String> getAllWorkerName(){
        return adminDao.getAllWorkerName();
    }
    @Override
    public int getOrderCount(String orderId,String ownerName,String workerName,int status){
        return adminDao.getOrderCount(orderId,ownerName,workerName,status);
    }
    @Override
    public ArrayList<OrderVo> getPagingOrderList(int currentPage, int pageSize, String orderId, String ownerName, String workerName, int status){
        ArrayList<OrderVo> orderVos =new ArrayList<>();
        orderVos=adminDao.getPagingOrderList((currentPage-1)*pageSize,pageSize,orderId,ownerName,workerName,status);
        System.out.println("遍历orderVos:");
        for(OrderVo orderVo: orderVos){
            System.out.println(orderVo);
        }
        //赋值业主姓名、维修工姓名、typeName
        for(OrderVo orderVo: orderVos){
            Owner owner=ownerDao.getOwnerById(orderVo.getOwnerId()) ;
            orderVo.setOwnerName(owner.getName());
            Worker worker = workerDao.getWorkerByPhone(orderVo.getWorkerId());
            orderVo.setWorkerName(worker.getName());
            String typeName = applyPropertyDao.getTypenameByTypeid(orderVo.getTypeId());
            orderVo.setTypeName(typeName);
            orderVo.setImgPath(applyPropertyDao.getImgPathByImgId(orderVo.getImgId()));
        }
        System.out.println("再次遍历：");
        for(OrderVo orderVo: orderVos){
            System.out.println(orderVo);
        }
        //遍历赋值业主姓名、维修工姓名
//        for(Order order:orders){
//            OrderVo orderVo3=new OrderVo(order);
//            orderVos.add(orderVo3);
//        }
        System.out.println("impl!!,orderVos:");
        System.out.println(orderVos);
        return orderVos;
    }

    @Override
    public int deleteOrders(String[] phones) {
        return adminDao.deleteOrders(phones);
    }

    @Override
    public int updateOrder(OrderVo orderVo) {
        if(!(orderVo.getAsess()==0)){
            orderVo.setIfAsess(true);
        }
        return adminDao.updateOrder1(orderVo)==1&&adminDao.updateOrder2(orderVo)==1?1:0;
    }







    //用不到

    @Override
    public ArrayList<feedBack> getAllFeedBack() {
        return adminDao.getAllFeedBack();
    }

    @Override
    public int backFeedBackByWorkId(int id, String backValue) {
        return adminDao.backFeedBackByWorkId(id,backValue);
    }

}
