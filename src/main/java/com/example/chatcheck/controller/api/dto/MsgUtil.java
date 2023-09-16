package com.example.chatcheck.controller.api.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

            history  = "刚才我们聊到：";
            history+= chatMsgs.subList(0, chatMsgs.size() - 1).stream().map(chatMsg -> {
                return chatMsg.getPContent();
            }).reduce((a, b) -> a + " " + b).get();
            history+=  "，我说：";
        }else {
            history = "：";
        }
        String message = chatMsgs.get(chatMsgs.size() - 1).getContent();
        String chatTem ="1、请你遵循回答的格式，把你的回答写在【】里面，例如：【你好】，其中换行用#代替，空格用&代替，回复尽量精准、简练，回复不能超过500字\n" +
                "2、当前问题编号：(%s#)，回答时请把编号放到回复前，例如“(%s#)【你好】”, 请严格遵守格式！\n" +
                "3、%s %s" ;
        String result = String.format(chatTem,key,key, history, message);

        return result;
    }


    public static String putMsgSme(String key, String msg) {

        List<ChatMsg> chatMsgs = JSONArray.parseArray(msg, ChatMsg.class);
        ChatMsg chatMsgNow = chatMsgs.get(chatMsgs.size() - 1);
        String msgNow = chatMsgNow.getContent();
        if(msgNow.length()>1500){
            if(msgNow.length()>2000){
                return  msgNow.substring(0,500)+"..." + msgNow.substring(msgNow.length()-1500,msgNow.length());
            }else {
                return  chatMsgNow.getContent();
            }
         }

        if(chatMsgs.size()>=4){
            chatMsgs = chatMsgs.subList(chatMsgs.size()-4, chatMsgs.size());
            ChatMsg system = chatMsgs.stream().filter(m -> m.getRole().equals("system")).findFirst().orElse(null);
            if(system!=null){
                List<ChatMsg> lastMsg = new ArrayList();
                lastMsg.add(system);
                lastMsg.addAll(chatMsgs.stream().filter(m -> !m.getRole().equals("system")).collect(Collectors.toList()));
                chatMsgs = lastMsg;
            }
        }
        String history = "";
        if(chatMsgs.size()>1){

            history  = "刚才我们聊到这里：";
            history+= chatMsgs.subList(0, chatMsgs.size() - 1).stream().map(chatMsg -> {
                return chatMsg.getPContent();
            }).reduce((a, b) -> a + " " + b).get();
            history+=  " 我说：";
        }else {
            history = "";
        }
        String message = history + chatMsgNow.getContent();
        message.replaceAll("&nbsp;","");
        message.replaceAll(" ","");
        if(message.length()>2000){
            message = message.substring(0,500)+"..." + message.substring(message.length()-1500,message.length());
          //  message = message.substring(message.length()-2000,message.length());
        }
        return message;
    }

    public static String msgItem(String old,String msg){
        String p1 = "chatcmpl-"+RandomUtil.randomString(30);
        long p2 = DateUtil.date().toTimestamp().getTime()/1000;
        if(msg.length()==old.length()){
           // return resFormat(p1,p2,"",null);
            return null;
        }
        String p3 ="";
        try {
            p3 = msg.substring(old.length());

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("error old:"+old+" now:"+msg);
        }

        //chatcmpl-NAztrAjDKxEH28czkN4POQ4n4nGwT
        return resFormat(p1,p2,p3,null);
    }

    private static String resFormat(String p1, long p2, String p3, String p4) {
//        p3= p3.replaceAll("Copy","  ");
//        p3= p3.replaceAll("\n\n","\n");
//        p3= p3.replaceAll(" ","&nbsp");

        String t3= p3.replaceAll(" ","&nbsp;").replaceAll(" ","&nbsp;").replaceAll(" ","&nbsp;");
        JSONObject res = new JSONObject();
        res.put("id",p1);
        res.put("object","chat.completion.chunk");
        res.put("created",p2);
        res.put("model","gpt-4.0");
        JSONObject delta = new JSONObject();
        delta.put("content",t3);
        JSONObject choice = new JSONObject();
        choice.put("delta",delta);
        choice.put("index",0);
        choice.put("finish_reason",p4);
        JSONArray choices = new JSONArray();
        choices.add(choice);
        res.put("choices",choices);

//        if(StrUtil.isBlank(p3)){
//            HashMap<String, Object> test = new HashMap<>();
//            test.put("id",p3.replaceAll("\\n","\\r"));
//            test.put("id2",p3.replaceAll("\n","\r"));
//            test.put("test",p3);
//            //   System.out.println("error p3:"+JSONObject.parseObject(JSONObject.toJSONString(test)));
//        }
//        HashMap<String, Object> test2 = new HashMap<>();
//        test2.put("p3",p3);
       // System.out.println(JSONObject.parseObject(JSONObject.toJSONString(test2)));
        return res.toJSONString();
    }


    public static String msgError(String msg){
        String error = String.format("{\"error\":{\"message\":\"%s\",\"type\":\"invalid_request_error\",\"param\":null,\"code\":\"Too Many Requests\"}}", msg);
        return error;
    }
    public static String msgItemEnd(){
        String done = "[DONE]";
        return done;
    }

    private static void printf(String item, PrintWriter writer) {
        if(StrUtil.isEmpty(item)){
            return;
        }
        //System.out.println(item);
        writer.write("data: " + item + "\n\n");
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

        int copyInt = StrUtil.count(last, "Copy");
        boolean copy =  copyInt%2==1;
        last=extracted(last,copy);
        now=extracted(now,copy);

        List<String> res = CollUtil.newArrayList();
        String p1 = "chatcmpl-"+RandomUtil.randomString(30);
        long p2 = DateUtil.date().toTimestamp().getTime()/1000;
        if(now.length()==last.length()){
            // return resFormat(p1,p2,"",null);
            return new ArrayList<>();
        }
        String p3 ="";
        try {
             p3 = now.substring(last.length());

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("error last:"+last+" now:"+now);
        }
        JSONObject res1 = new JSONObject();
        res1.put("last",last);
        res1.put("now",now);
       // System.out.println("error last:"+res1.toJSONString());

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

    private static String extracted(String p3,boolean copy) {
        if(copy){
            p3= p3.replaceAll("\n"," ");
        }
        p3= p3.replaceAll("Copy","  ");
        return p3;
    }


}
