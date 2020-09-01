package com.yc.property.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    private String cardId;
    private String name;
    private String sex;
    private String phone;
    private String password;
    private String mail;
    private int roomId;
    private int status;
}
