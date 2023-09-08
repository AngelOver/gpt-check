package com.example.chatcheck.controller;

import com.example.chatcheck.util.GPT3ClientUtil;
import org.springframework.web.bind.annotation.GetMapping;

public class NodeController {

    /**
     * 设备资源接口-对外接口-分页查询
     *
     * @return 所有数据
     */
    @GetMapping("/chat")
    public String chat(String msg) {

        String s = GPT3ClientUtil.sendMsg("1", msg);
        return s;
    }
}
