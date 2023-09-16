package com.example.chatcheck.controller.api.dto;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgApiOnlineDTO {

    //客户端上报内容
    public String text;
    //客户端Id
    public String clientId;
    //解析后的内容 Id
    public String msgId;
    //解析后的内容

    public String msg;

    public boolean freeFlag;

    public boolean allTextFlag;


    boolean getMsgIsOk() {
        return StrUtil.isNotEmpty(text)&&text.contains("】");
    }



    public void initText() {
        if (StrUtil.isNotEmpty(msgId)) {
//            if (text.contains("#")) {
//                msgId = text.split("#\\)")[0].replace("(", "");
//            }
//            if (text.contains("【") ) {
//                msg = text.split("【")[1];
//            }
            msg = text;
        }

    }
}
