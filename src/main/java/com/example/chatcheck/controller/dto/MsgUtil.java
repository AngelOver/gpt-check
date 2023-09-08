package com.example.chatcheck.controller.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MsgUtil {


    static String[] msgList =new String[]{
            "hello","say hello","how are you","what","when","what",
            "red", "blue", "green", "yellow", "orange",
            "apple", "banana", "orange", "kiwi", "grape",
            "January", "February", "March", "April", "May",
            "football", "basketball", "volleyball", "tennis", "baseball",
            "dog", "cat", "bird", "hamster", "rabbit",
            "China", "USA", "Canada", "Japan", "UK",
            "pizza", "burger", "sushi", "noodles", "rice"
    };

    public static String randomMsg() {
        String msg;
        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
        msg = CollUtil.newArrayList(msgList).get(randomKey);
        return msg;
    }


    public static String putMsg(String key, String msg) {
        List<ChatMsg> chatMsgs = JSONArray.parseArray(msg, ChatMsg.class);
        chatMsgs.subList(chatMsgs.size()-1, chatMsgs.size());
        if(chatMsgs.size()>=2){
            chatMsgs = chatMsgs.subList(chatMsgs.size()-2, chatMsgs.size());
            //           chatMsgs =     chatMsgs.subList(chatMsgs.size()-1, chatMsgs.size());
        }
        String history = "";
        if(chatMsgs.size()>1){

            history  = "跟着我们之前的思路，我回顾了之前的对话：";
            history+= chatMsgs.subList(0, chatMsgs.size() - 1).stream().map(chatMsg -> {
                return chatMsg.getPContent();
            }).reduce((a, b) -> a + " " + b).get();
            history+=  "，我说：";
        }else {
            history = "我开始提问：";
        }
        String message = chatMsgs.get(chatMsgs.size() - 1).getContent();
        String chatTem ="1、请你遵循回答的格式，把你的回答写在【】里面，例如：【你好】，其中换行用#代替，空格用&代替，回复尽量精准、简练，回复不能超过500字\n" +
                "2、当前问题编号：(%s#)，回答时请把编号放到回复前，例如“(%s#)【你好】”, 请严格遵守格式！\n" +
                "3、%s %s" ;
        String result = String.format(chatTem,key,key, history, message);

        return result;
    }

    public static String msgItem(String old,String msg){
        String p1 = "chatcmpl-"+RandomUtil.randomString(30);
        long p2 = DateUtil.date().toTimestamp().getTime()/1000;
        if(msg.length()==old.length()){
           // return resFormat(p1,p2,"",null);
            return null;
        }
        String p3 = msg.substring(old.length());
        //chatcmpl-NAztrAjDKxEH28czkN4POQ4n4nGwT
        return resFormat(p1,p2,p3,null);
    }

    private static String resFormat(String p1, long p2, String p3, String p4) {
        JSONObject res = new JSONObject();
        res.put("id",p1);
        res.put("object","chat.completion.chunk");
        res.put("created",p2);
        res.put("model","gpt-4.0");
        JSONObject delta = new JSONObject();
        delta.put("content",p3.replace("#","\n").replace("&"," "));
        JSONObject choice = new JSONObject();
        choice.put("delta",delta);
        choice.put("index",0);
        choice.put("finish_reason",p4);
        JSONArray choices = new JSONArray();
        choices.add(choice);
        res.put("choices",choices);
        return res.toJSONString();
    }

    public static String msgItemEnd(){
        String done = "[DONE]";
        return done;
    }

    private static void printf(String item, PrintWriter writer) {
        if(StrUtil.isBlank(item)){
            return;
        }
        //System.out.println(item);
        writer.write("data: " + item.replace("】","") + "\n\n");
        writer.flush();
    }

    private static void printf(String last,String now, PrintWriter writer) {
        printf(  MsgUtil.msgItem(last, now),writer);
    }

    public static void printfSteam(String last,String now, PrintWriter writer) {
        List<String> res = MsgUtil.msgItemList(last, now);
        res.forEach(item->{
            printf(item,writer);
        });
    }

    private static List<String> msgItemList(String last, String now) {
        List<String> res = CollUtil.newArrayList();
        String p1 = "chatcmpl-"+RandomUtil.randomString(30);
        long p2 = DateUtil.date().toTimestamp().getTime()/1000;
        if(now.length()==last.length()){
            // return resFormat(p1,p2,"",null);
            return new ArrayList<>();
        }
        String p3 = now.substring(last.length());
        int i = 0;
        while (i<p3.length()){
            if(i+2>=p3.length()){
                res.add(resFormat(p1,p2,p3.substring(i,p3.length()),null));
                break;
            }
            res.add(resFormat(p1,p2,p3.substring(i,i+2),null));
            i=i+2;
        }
        return res;
    }


}
