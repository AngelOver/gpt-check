package com.example.chatcheck.controller.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.controller.RedisCacheService;
import com.example.chatcheck.controller.api.dto.MsgApiOnlineDTO;
import com.example.chatcheck.controller.api.dto.MsgUtil;
import com.example.chatcheck.controller.api.dto.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 设备列表对外接口，对其他模块
 */
@RestController
@RequestMapping("/v1/chat")
public class ChatApiController {

    @Autowired
    private RedisCacheService redisCacheService;




    /**
     * api接口
     * @param param
     * @return
     */
    @PostMapping("/completions")
    public void chat(@RequestBody JSONObject param, HttpServletResponse response, HttpServletRequest request ) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int timeOutAll = 150; //120秒判断客户端异常
        int timeOut = 6; //15秒超时
        JSONArray messages = param.getJSONArray("messages");
        String authorization = request.getHeader("Authorization");
        if(StrUtil.isEmpty(authorization)){
            returnError(response, writer, "错误的ApiKey");
            return;
        }
        if(!authorization.contains("1chat")){
            returnError(response, writer, "错误的ApiKey");
            return;
        }

        //System.out.println(JSONArray.toJSONString(CollUtil.getLast(messages)));
        //redisCacheService.set(msgKey,messages.toJSONString()); // 多服务端情况启用
        MsgDTO msgDTO = new MsgDTO( messages.toJSONString()).putClientMsg();
        if(msgDTO.getClientMsg().contains("hi")){
            printf(  "hi",writer);
            printf(MsgUtil.msgItemEnd(),writer);
            writer.close();
            return;
        }
        String msg = "";
        if (sendMsg(response, writer,  timeOutAll, timeOut,  msgDTO)){
            msgDTO = new MsgDTO( messages.toJSONString()).putClientMsg();
            if(sendMsg(response, writer,  timeOutAll, timeOut,  msgDTO)){
                msgDTO = new MsgDTO( messages.toJSONString()).putClientMsg();
                if(sendMsg(response, writer, timeOutAll, timeOut, msgDTO)){
                    if(sendMsg(response, writer, timeOutAll, timeOut, msgDTO)){
                        msg = "服务器正忙，请等待10秒再试.....";
                        returnError(response, writer, msg);
                    }
                }
            }

            return;
        }
        //return msg;
        writer.close();
    }

    private static void returnError(HttpServletResponse response, PrintWriter writer, String msg) {
        response.setStatus(429);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        writer.write(MsgUtil.msgError(msg));
        writer.close();
    }

    private boolean sendMsg(HttpServletResponse response, PrintWriter writer,  int timeOutAll, int timeOut,   MsgDTO msgDTO) {
        String msg = "";
        TimeInterval timer = DateUtil.timer();
        MsgUtil.msgMap.put(msgDTO.key,msgDTO);
        String msgKey = msgDTO.key;
        boolean startFlag = false;
        String m = "";
        try {
            while(true) {
                String last = msg;
                double time = timer.intervalMs()*1.0/1000;
                if(time> timeOut &&!startFlag){

                    return true;
                }
                if(time> timeOutAll){
                    msg = "服务器线程已满，执行超时！！！";
                    printf(  MsgUtil.msgItem(last, msg), writer);
                    break;
                }
                boolean b = redisCacheService.hasKey(msgKey);
                if(b){
                    if(!startFlag){
                        startFlag = true;
                        //System.out.println(msgKey +"开始响应=========");
                    }
                    if(msg.contains("rate limit")){
                        return true;
                    }
                    String nowMsg = (String) redisCacheService.get(msgKey);
                    msg = nowMsg;
                    MsgUtil.printfSteam(  last, msg, writer);
                    //System.out.println("开始响应=========");
                    //System.out.println(nowMsg);
                    if(redisCacheService.hasKey(msgKey +"ok")){
                        msg = nowMsg;
                       // System.out.println(msgKey +"完成==");
                        break;
                    }
                }
                ThreadUtil.sleep(200);
            }
        }catch (Exception e){
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
            clearKeyClient(msgKey);
            System.out.println("耗时(s)：{}"+ timer.intervalMs()*1.0/1000+ msgKey);
        }
        //System.out.println("回复：{}"+msg);

        printf(MsgUtil.msgItemEnd(), writer);
        return false;
    }

    //
//
//    /**
//     * api接口
//     * @param param
//     * @return
//     */
//    @PostMapping("/completions2")
//    public String chat2(@RequestBody JSONObject param,HttpServletResponse response ) {
//
//
//        String msg = "";
//        int timeOutAll = 60; //120秒判断客户端异常
//        int timeOut = 15; //15秒超时
//        JSONArray messages = param.getJSONArray("messages");
//
//        String msgKey = RandomUtil.simpleUUID().substring(0, 4);
//        TimeInterval timer = DateUtil.timer();
//        System.out.println("待处理:"+msgQueue.size());
//        System.out.println(JSONArray.toJSONString(CollUtil.getLast(messages)));
//        //redisCacheService.set(msgKey,messages.toJSONString()); // 多服务端情况启用
//        MsgDTO msgDTO = new MsgDTO(msgKey, messages.toJSONString()).putClientMsg();
//        msgQueue.add(msgDTO);
//        boolean startFlag = false;
//        try {
//            while(true) {
//                double time =timer.intervalMs()*1.0/1000;
//                if(time>timeOut&&!startFlag){
//                    msg= "服务器正忙，请稍后再试";
//                    clearKeyClient(msgKey);
//                    break;
//                }
//                if(time>timeOutAll){
//                    msg= "服务器错误！！！";
//                    break;
//                }
//                boolean b = redisCacheService.hasKey(msgKey);
//                if(b){
//                    startFlag = true;
//                    String nowMsg = (String) redisCacheService.get(msgKey);
//
//                    System.out.println("开始响应=========");
//                    System.out.println(nowMsg);
//                    if(nowMsg.contains("】")){
//                        msg = nowMsg.replace("】","");
//                        break;
//                    }
//                }
//                ThreadUtil.sleep(200);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            clearKeyClient(msgKey);
//        }
//        System.out.println("回复：{}"+msg);
//        System.out.println("耗时(s)：{}"+timer.intervalMs()*1.0/1000);
//        return msg;
//    }
//
    private void clearKeyClient(String msgKey) {
        if(redisCacheService.hasKey(msgKey+"client")){
            redisCacheService.set((String)redisCacheService.get(msgKey+"client" ),"冷却",3,  TimeUnit.SECONDS);
            //redisCacheService.delete((String)redisCacheService.get(msgKey+"client" ) );
            System.out.println("清除客户端："+redisCacheService.get(msgKey +"client"));
        }
       // redisCacheService.delete("waiting"+msgKey);
       // System.out.println(JSONObject.toJSONString(msgMap));
        MsgUtil.msgMap.remove(msgKey);
    }

//    @PostMapping("/chat4")
//    public void chat4(String msg, HttpServletResponse response) throws IOException {
//        printfMsg(msg, response);
////        response.getOutputStream().write(new String("1").getBytes(StandardCharsets.UTF_8));
////        response.getOutputStream().flush();
////        response.getOutputStream().write(new String("3").getBytes(StandardCharsets.UTF_8));
////        response.getOutputStream().flush();
////        response.getOutputStream().write(new String("4").getBytes(StandardCharsets.UTF_8));
////        response.getOutputStream().flush();
////        GPTPlusUtil.sendMsg4(msg, response);
//    }

    private static void printfMsgSteam(String msg, HttpServletResponse response)  {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

        String msgR ="";
        for (int i = 0; i < msg.length(); i++) {
            String last = msgR;
            msgR+= msg.substring(i,i+1);
            String s = MsgUtil.msgItem(last, msgR);
            printf(s,writer);
        }
        printf( MsgUtil.msgItem(msgR, msg),writer);
        printf(MsgUtil.msgItemEnd(),writer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    private static void printf(String item, PrintWriter writer) {
        if(StrUtil.isBlank(item)){
            return;
        }
        writer.write("data: " + item.replace("】","") + "\n\n");
        writer.flush();
    }

}
