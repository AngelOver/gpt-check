package com.example.chatcheck.controller;

import cn.hutool.core.util.RandomUtil;
import com.example.chatcheck.controller.api.dto.MsgUtil;
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

    public MsgDTO(String toJSONString) {
        key = "que+"+ RandomUtil.simpleUUID().substring(0, 4);
        value = toJSONString;
    }

    public MsgDTO putClientMsg(){
        this.clientMsg= MsgUtil.putMsgSme(key,value);
        return this;
    }
}
