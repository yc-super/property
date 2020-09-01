package com.yc.property.vo;


import com.yc.property.Bean.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVo {
    private String id;
    private String password;
    private String token;

    public AdminVo(Admin admin,String token){
        this.id=admin.getId();
        this.password=admin.getPassword();
        this.token=token;
    }
}
