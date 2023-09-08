package com.example.chatcheck.controller.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {
    String role ;
    String content ;

    public String getPContent() {
        if(role.equals("user")){
            return "我："+content;
        }else {
            return "你："+content;
        }
    }

//    String chat ="1、请你遵循回答的格式，把你的回答写在【】里面，例如：【你好】，其中换行 用!#!代替，空格用!@!代替，回复不能超过500字\n" +
//            "2、跟着我们之前的思路，我回顾了之前的对话：我：java是什么 你：Java是一种广泛使用的编程语言，它的设计使开发人员可以在任何地方编写代码，一次编写，到处运行（WORA，Write Once, Run Anywhere）。这是通过Java虚拟机（JVM）实现的，它可以将Java代码转换为任何设备或操作系统能理解的代码。我说，%s"


}
