package com.yc.property.vo;

import com.yc.property.Bean.Worker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerVo {
    private String cardId;
    private String name;
    private String sex;
    private String phone;
    private String password;
    private String mail;
    private String status;
    private String token;

    public WorkerVo(Worker worker,String token){
        this.cardId=worker.getCardId();
        this.name=worker.getName();
        this.sex=worker.getSex();
        this.phone=worker.getPhone();
        this.password=worker.getPassword();
        this.mail=worker.getMail();
        this.status=worker.getStatus()==1?"正常":"已删除";
        this.token=token;
    }
    public WorkerVo(Worker worker){
        this.cardId=worker.getCardId();
        this.name=worker.getName();
        this.sex=worker.getSex();
        this.phone=worker.getPhone();
        this.password=worker.getPassword();
        this.mail=worker.getMail();
        this.status=worker.getStatus()==1?"正常":"已删除";
    }
}
