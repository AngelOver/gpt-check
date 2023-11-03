package com.example.chatcheck.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.controller.dto.ChatMsg;
import com.example.chatcheck.controller.dto.MsgApiOnlineDTO;
import com.example.chatcheck.controller.dto.MsgUtil;
import com.example.chatcheck.controller.dto.R;
import com.example.chatcheck.util.GPTPlusUtil;
import io.github.furstenheim.CopyDown;
import io.github.furstenheim.Options;
import io.github.furstenheim.OptionsBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 设备列表对外接口，对其他模块
 */
@RestController
@RequestMapping("/v1/chat")
public class ChatApiController {

    @Autowired
    private RedisCacheService redisCacheService;


    public static PriorityQueue<MsgDTO> msgQueue = new PriorityQueue<>();


    public static List<MsgDTO> MSGList = new ArrayList<>();


    @GetMapping("/test")
    public String baidu(String msg ) {
        System.out.println(msg);
        return "你好，我是小智，很高兴为您服务";
    }

    /**
     * 客户端监控-实时获取屏幕数据
     * @param req 客户端上报的信息
     * @return
     */
    @PostMapping("/online2")
    public String online2(@RequestBody MsgApiOnlineDTO req)  {

        req.initText();
        String clientId = req.getClientId();
        String text = req.getText();
        String msgId = req.getMsgId();
        String msg = req.getMsg();
        String returnMsg =  MsgUtil.randomMsg();
        //System.out.println(JSONObject.toJSONString(req));
        // 监控端 空闲、处理中
        // 检查到编号，并且开始回答了，就把回答的内容存入redis
        if(StrUtil.isEmpty(clientId)){
            return returnMsg;
        }
        // 客户端只负责把id和内容上报，不做处理
        if(StrUtil.isNotEmpty(msgId)){
            redisCacheService.set(msgId,msg);
        }
        if(req.freeFlag){
            //客户端已经释放，请下发任务
            if(redisCacheService.hasKey(req.clientId)){
                redisCacheService.delete(req.clientId);
            }
        }
        return returnMsg; //下发问题
    }

    /**
     * 客户端监控-实时获取屏幕数据
     * @param req 客户端上报的信息
     * @return
     */
    @PostMapping("/send")
    public R send(@RequestBody MsgApiOnlineDTO req)  {
        req.initText();
        String clientId = req.getClientId();
        String text = req.getText();
        String msgId = req.getMsgId();
        String msg = req.getMsg();
        String returnMsg =  MsgUtil.randomMsg();
        //客户端试图领取任务
        if(StrUtil.isEmpty(clientId)){
            return R.ok();
        }
        if(redisCacheService.hasKey(clientId)){
        //    System.out.println(clientId+"服务端认为客户端 忙碌中======");
            return R.er("客户端忙碌中");
        }
        if(!req.freeFlag){
         //   System.out.println(clientId+"客户端 忙碌中======");
            return R.er("客户端忙碌中");
        }
        // 客户端空闲，从队列中取出任务
        if(MSGList.size()==0){
           // System.out.println(clientId+"客户端 无任务 等待中======");
            return R.er("客户端 无任务 等待中");
        }
        MsgDTO peek = MSGList.get(0);
        MSGList.remove(0);
        redisCacheService.set(peek.key,"");//任务开始响应
        redisCacheService.set(clientId,peek.key);//客户端开始占用
        redisCacheService.set(peek.key+"client",clientId);//问题开始被客户端接管
        System.out.println(clientId+"客户端 执行任务 msgId:"+peek.key+"======");
        return R.ok(peek); //下发问题
    }



    /**
     * 客户端监控-实时获取屏幕数据
     * @param req 客户端上报的信息
     * @return
     */
    @PostMapping("/online")
    public String online(@RequestBody MsgApiOnlineDTO req)  {
        req.initText();
        String clientId = req.getClientId();
        String text = req.getText();
        String msgId = req.getMsgId();
        String msg = req.getMsg();
        String returnMsg =  MsgUtil.randomMsg();
        System.out.println(clientId+"客户端定时上报");
        //System.out.println(JSONObject.toJSONString(req));
        // 监控端 空闲、处理中
        // 检查到编号，并且开始回答了，就把回答的内容存入redis
        if(StrUtil.isEmpty(clientId)){
            return returnMsg;
        }
        // 客户端只负责把id和内容上报，不做处理
        if(StrUtil.isNotEmpty(msgId)){
            redisCacheService.set(msgId,msg);
        }
        if(redisCacheService.hasKey(clientId)){
            System.out.println(clientId+"客户端 任务处理中======");
            System.out.println(returnMsg);
            return returnMsg;
        }
        // 客户端空闲，从队列中取出任务
        if(MSGList.size()==0){
            System.out.println(clientId+"客户端 无任务 等待中======");
            return returnMsg;
        }
        MsgDTO peek = MSGList.get(0);
        MSGList.remove(0);
        redisCacheService.set(peek.key,"");//任务开始响应
        redisCacheService.set(clientId,peek.key);//客户端开始占用
        redisCacheService.set(peek.key+"client",clientId);//问题开始被客户端接管
        System.out.println(clientId+"客户端 执行任务 msgId:"+peek.key+"======");
        return peek.getClientMsg(); //下发问题
    }

    @PostMapping("/completions")
    public void chat4(@RequestBody Object msg, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
        JSONArray messages = jsonObject.getJSONArray("messages");
        JSONArray newMsg = new JSONArray();
        int index = 0;
        for (Object message : messages) {
            index = index+1;
            if(index==1){
                JSONObject data =JSONObject.parseObject(JSONObject.toJSONString(message)) ;
                if("system".equals(data.getString("role"))  ){
                    data.put("content", data.getString("content")+
                            " You are ChatGPT, a large language model trained by OpenAI.Knowledge cutoff: 2021-09,Current model: gpt-4"
                    );
                    newMsg.add(data);
                    continue;
                }
            }
            newMsg.add(message);
        }
        jsonObject.put("messages", newMsg);
        GPTPlusUtil.sendMsg4(JSONObject.toJSONString(jsonObject), response);
    }
    /**
     * api接口
     * @param param
     * @return
     */
    @PostMapping("/completions2")
    public void chat(@RequestBody JSONObject param,HttpServletResponse response ) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String msg = "";
        int timeOutAll = 60; //120秒判断客户端异常
        int timeOut = 7; //15秒超时
        JSONArray messages = param.getJSONArray("messages");

        String msgKey = RandomUtil.simpleUUID().substring(0, 4);
        TimeInterval timer = DateUtil.timer();
        System.out.println("待处理:"+msgQueue.size());
        System.out.println(JSONArray.toJSONString(CollUtil.getLast(messages)));
        //redisCacheService.set(msgKey,messages.toJSONString()); // 多服务端情况启用
        MsgDTO msgDTO = new MsgDTO(msgKey, messages.toJSONString()).putClientMsg();
        if(msgDTO.getClientMsg().contains("我开始提问： hi")){
            printf(  "hi",writer);
            printf(MsgUtil.msgItemEnd(),writer);
            writer.close();
            return;
        }
        MSGList.add(msgDTO);
        boolean startFlag = false;
        String m = "";
        try {
            while(true) {
                String last = msg;
                double time =timer.intervalMs()*1.0/1000;
                if(time>timeOut&&!startFlag){
                    msg= "服务器正忙，请等待10秒再试.....";
                    printf(  MsgUtil.msgItem(last, msg),writer);
                    break;
                }
                if(time>timeOutAll){
                    msg= "服务器线程已满，执行超时！！！";
                    printf(  MsgUtil.msgItem(last, msg),writer);
                    break;
                }
                boolean b = redisCacheService.hasKey(msgKey);
                if(b){
                    if(!startFlag){
                        startFlag = true;
                        System.out.println(msgKey+"开始响应=========");
                    }

                    String nowMsg = (String) redisCacheService.get(msgKey);
                    msg = nowMsg;
                    MsgUtil.printfSteam(  last, msg,writer);
                    //System.out.println("开始响应=========");
                    //System.out.println(nowMsg);
                    if(nowMsg.contains("】")){
                        msg = nowMsg.replace("】","");
                        System.out.println(msgKey+"完成==");
                        break;
                    }
                }
                ThreadUtil.sleep(200);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            clearKeyClient(msgKey);
        }
        System.out.println("回复：{}"+msg);
        System.out.println("耗时(s)：{}"+timer.intervalMs()*1.0/1000);
        printf(MsgUtil.msgItemEnd(),writer);
        //return msg;
        writer.close();
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
            redisCacheService.delete((String)redisCacheService.get(msgKey+"client" ) );
            System.out.println("清除客户端："+redisCacheService.get(msgKey +"client"));
        }
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
        printf(  MsgUtil.msgItem(msgR, msg),writer);
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
        System.out.println(item);
        writer.write("data: " + item.replace("】","") + "\n\n");
        writer.flush();
    }

}
