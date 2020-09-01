package com.yc.property.Service;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;

import java.util.ArrayList;


public interface OwnerService {
    Owner getOwnerById(String phone);
    Owner getOwnerByPhone(String phone);
    int regeisterOwner(String  phone,String pwd);
    Owner ifPasswordTrue(String cardId,String password);
    int reSetPassword(Owner owner);
    ArrayList<OrderVo> findOldOrderByPhone(String phone);
    int updateOwnerMessage(Owner owner);
    Owner getOwenrByRoomId(int roomId);
}
