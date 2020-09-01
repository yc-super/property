package com.yc.property.Dao;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Mapper
public interface OwnerDao {
    Owner getOwnerById(String phone);
    Owner getOwnerByPhone(String phone);
    int regeisterOwner(String  phone,String pwd);
    Owner ifPasswordTrue(String cardId,String password);
    Owner forgetPassword(OwnerVo ownerVo);
    int reSetPassword(Owner owner);
    int updateOwnerStatus(int status,String phone);
    ArrayList<OrderVo> findOldOrderByPhone(String phone);
    int updateOwnerMessage(Owner owner);
    //根据roomId查buildingId和address
    OwnerVo getValueByRoomId(int roomId);
    //根据building和address查roomId
    int getRoomIdByBuildingIdAndAddress(int buildingId,int address);
    //通过roomId查找owner
    Owner getOwnerByRoomId(int roomId);
}
