package com.yc.property.vo;

import com.yc.property.Bean.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerVo {
    private String cardId;
    private String name;
    private String sex;
    private String phone;
    private String password;
    private String mail;
    private int roomId;
    private int buildingId;
    private int address;
    private String status;
    private String token;

    public OwnerVo(Owner owner,OwnerVo ownerVo,String token){
        this.cardId=owner.getCardId();
        this.name=owner.getName();
        this.sex=owner.getSex();
        this.phone=owner.getPhone();
        this.password=owner.getPassword();
        this.mail=owner.getMail();
        this.status=owner.getStatus()==1?"正常":(owner.getStatus()==0?"已删除":"未审核");
        this.buildingId=ownerVo.getBuildingId();
        this.address=ownerVo.getAddress();
        this.token=token;
    }
    public OwnerVo(Owner owner,OwnerVo ownerVo){
        this.cardId=owner.getCardId();
        this.name=owner.getName();
        this.sex=owner.getSex();
        this.phone=owner.getPhone();
        this.password=owner.getPassword();
        this.mail=owner.getMail();
        this.status=owner.getStatus()==1?"正常":(owner.getStatus()==0?"已删除":"未审核");
        this.buildingId=ownerVo.getBuildingId();
        this.address=ownerVo.getAddress();
    }
    public OwnerVo(Owner owner,String token){
        this.cardId=owner.getCardId();
        this.name=owner.getName();
        this.sex=owner.getSex();
        this.phone=owner.getPhone();
        this.password=owner.getPassword();
        this.mail=owner.getMail();
        this.status=owner.getStatus()==1?"正常":(owner.getStatus()==0?"已删除":"未审核");
        this.token=token;
    }
}
