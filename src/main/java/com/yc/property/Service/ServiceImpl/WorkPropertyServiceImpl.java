package com.yc.property.Service.ServiceImpl;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.ApplyPropertyDao;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Dao.WorkPropertyDao;
import com.yc.property.Service.WorkPropertyService;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkPropertyServiceImpl implements WorkPropertyService {
    @Autowired
    private WorkPropertyDao workPropertyDao;
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private ApplyPropertyDao applyPropertyDao;
    @Override
    public ArrayList<OrderVo> getOrderList(String workerPhone) {
        ArrayList<OrderVo> orderVos=workPropertyDao.getOrderList(workerPhone);
        orderVos=getCompleteOrder(orderVos);
        return orderVos;
    }

    @Override
    public ArrayList<OrderVo> getTypeOrderList(String workerPhone,String keyWords,String values) {
        ArrayList<OrderVo> orderVos=new ArrayList<OrderVo>();
        if(keyWords.equals("待维修")){
            orderVos=workPropertyDao.getUnfinishedOrderByWorkerPhone(workerPhone);
        }else if(keyWords.equals("已维修")){
            orderVos=workPropertyDao.getFinishedOrderByWorkerPhone(workerPhone);
        }else if(keyWords.equals("用户已取消")){
            orderVos=workPropertyDao.getCancelOrderByWorkerPhone(workerPhone);
        }else if(keyWords.equals("查询全部订单")){
            orderVos=getOrderList(workerPhone);
        }else if(keyWords.equals("按订单号搜索")){
            System.out.println("values:"+values);
            String orderId=values;
            OrderVo orderVo2=workPropertyDao.isOrderExistByOrderIdAndWorkerPhone(orderId,workerPhone);
            if(orderVo2==null){
                return orderVos;
            }else{
                orderVos=workPropertyDao.getOrderDetailByOrderId(orderId);
            }
        }else if(keyWords.equals("按楼号搜索")){
            System.out.println("values:"+values);
            int buildingId=Integer.parseInt(values);
            orderVos=workPropertyDao.getOrderByBuildingIdAndWorkerPhone(buildingId,workerPhone);
        }else if(keyWords.equals("按业主姓名搜索")){
            System.out.println("values:"+values);
            String ownerName=values;
            orderVos=workPropertyDao.getOrderByOwnerNameAndWorkerPhone(ownerName,workerPhone);
        }else {
            System.out.println("keywords不合法！");
            return orderVos;
        }
        orderVos=getCompleteOrder(orderVos);
        return orderVos;
    }

    private ArrayList<OrderVo> getCompleteOrder(ArrayList<OrderVo> orderVos){
        for(OrderVo orderVo:orderVos){
            String orderId=orderVo.getOrderId();
            Worker worker=workPropertyDao.getWorkerByOrderId(orderId);
            Owner owner=workPropertyDao.getOwnerByOrderId(orderId);
            int status=workPropertyDao.getStatusByOrderId(orderId);
            int typeId=orderVo.getTypeId();
            String typeName=workPropertyDao.getTypeNameByTypeId(typeId);
            orderVo.setStatus(status);
            orderVo.setTypeName(typeName);
            orderVo.setWorkerName(worker.getName());
            orderVo.setWorkerId(worker.getPhone());
            orderVo.setOwnerName(owner.getName());
            orderVo.setOwnerId(owner.getPhone());
            OwnerVo ownerVo=ownerDao.getValueByRoomId(owner.getRoomId());
            orderVo.setBuildingId(ownerVo.getBuildingId());
            orderVo.setAddress(ownerVo.getAddress());

            orderVo.setImgPath(applyPropertyDao.getImgPathByImgId(orderVo.getImgId()));
        }
        return orderVos;
    }

    @Override
    public int feedBack(String feedBack, String cardId) {
        return workPropertyDao.feedBack(feedBack,cardId);
    }
}
