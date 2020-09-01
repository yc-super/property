package com.yc.property.Service.ServiceImpl;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.ApplyPropertyDao;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Dao.WorkPropertyDao;
import com.yc.property.Dao.WorkerDao;
import com.yc.property.Service.OwnerService;
import com.yc.property.Service.WorkerService;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private WorkPropertyDao workPropertyDao;
    @Autowired
    private ApplyPropertyDao applyPropertyDao;
    @Override
    public Owner getOwnerById(String phone) {
        return ownerDao.getOwnerById(phone);
    }
    @Override
    public Owner getOwenrByRoomId(int roomId){
        return ownerDao.getOwnerByRoomId(roomId);
    }

    @Override
    public Owner getOwnerByPhone(String phone) {
        return ownerDao.getOwnerByPhone(phone);
    }

    @Override
    public int regeisterOwner(String phone, String pwd) {
        return ownerDao.regeisterOwner(phone,pwd);
    }

    @Override
    public Owner ifPasswordTrue(String cardId, String password) {
        return ownerDao.ifPasswordTrue(cardId,password);
    }


    @Override
    public int reSetPassword(Owner owner) {
        return ownerDao.reSetPassword(owner);
    }

    @Override
    public ArrayList<OrderVo> findOldOrderByPhone(String phone) {
        ArrayList<OrderVo> orderVos=new ArrayList<>();
        orderVos=ownerDao.findOldOrderByPhone(phone);
        orderVos=getCompleteOrder(orderVos);
        return orderVos;
    }

    @Override
    public int updateOwnerMessage(Owner owner) {
        return ownerDao.updateOwnerMessage(owner);
    }

    private ArrayList<OrderVo> getCompleteOrder(ArrayList<OrderVo> orderVos){
        for(OrderVo orderVo:orderVos){
            String orderId=orderVo.getOrderId();
            System.out.println("!!!orderId:"+orderId);
            Worker worker=workPropertyDao.getWorkerByOrderId(orderId);
            System.out.println("worker:");
            System.out.println(worker);
            Owner owner=workPropertyDao.getOwnerByOrderId(orderId);
            OwnerVo ownerVo=ownerDao.getValueByRoomId(owner.getRoomId());
            int status=workPropertyDao.getStatusByOrderId(orderId);
            int typeId=orderVo.getTypeId();
            String typeName=workPropertyDao.getTypeNameByTypeId(typeId);
            orderVo.setStatus(status);
            orderVo.setTypeName(typeName);
            orderVo.setWorkerName(worker.getName());
            orderVo.setWorkerId(worker.getPhone());
            orderVo.setOwnerName(ownerVo.getName());
            orderVo.setOwnerId(ownerVo.getPhone());
            orderVo.setBuildingId(ownerVo.getBuildingId());
            orderVo.setAddress(ownerVo.getAddress());

            orderVo.setImgPath(applyPropertyDao.getImgPathByImgId(orderVo.getImgId()));
        }
        return orderVos;
    }
}
