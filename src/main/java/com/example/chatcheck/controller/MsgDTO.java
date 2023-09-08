package com.example.chatcheck.controller;

import com.example.chatcheck.controller.dto.MsgUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgDTO {

    String key ;
    String value ;

    String clientMsg;

    public MsgDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public MsgDTO putClientMsg(){
        this.clientMsg= MsgUtil.putMsg(key,value);
        return this;
    }
}
