package com.example.chatcheck.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.controller.dto.MsgApiOnlineDTO;
import com.example.chatcheck.controller.dto.MsgUtil;
import com.example.chatcheck.controller.dto.R;
import com.example.chatcheck.util.GPTOKStreamUtil;
import com.example.chatcheck.util.GPTPlusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

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
   static  final String gpt4 = " You are ChatGPT, a large language model trained by OpenAI. Current model: gpt-4";

    @PostMapping("/completions_old")
    public void chat4(@RequestBody Object msg, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        try {
            response.setContentType("text/event-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
            JSONArray messages = jsonObject.getJSONArray("messages");
            JSONArray newMsg = new JSONArray();
            boolean isN1106 = true;//仅仅当模型文字小于500token，并最后一行小于500
            boolean isN1106All = false;//当出现gpt时，一定调用1106
            int index = 0;
            System.out.println(messages.size());
            for (Object message : messages) {
                index = index+1;
                JSONObject data =JSONObject.parseObject(JSONObject.toJSONString(message)) ;
                if(index==1){

                    if("system".equals(data.getString("role"))  ){
                        data.put("content", data.getString("content")+gpt4 );
                        newMsg.add(data);
                        continue;
                    }
                }
                String content = data.getString("content");
                if(content.contains("gpt")||content.contains("GPT")||content.contains("知识库")||content.contains("2023")){
                    isN1106All=true;
                }else{
                    int length =  content.length();
                    if(length>350){
                        isN1106 = false;
                    }
                }

                newMsg.add(message);
            }
            System.out.println(DateUtil.now()+":"+JSONObject.toJSONString(newMsg.get(newMsg.size()-1)));
            jsonObject.put("messages", newMsg);
            boolean isN = isN1106All ? true : isN1106;
            GPTPlusUtil.sendMsg4Nine(isN,jsonObject, response);
        } catch (Exception e)
        {

            System.out.println("error"+e.getMessage());
            response.getWriter().close();
        }
    }

    @PostMapping("/completions")
    public void see2(@RequestBody Object msg, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        try {
            String authorization = request.getHeader("authorization");

            response.setContentType("text/event-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
           // TimeInterval timer = DateUtil.timer();
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
            JSONArray messages = jsonObject.getJSONArray("messages");
            JSONArray newMsg = new JSONArray();
            boolean isN1106 = true;//仅仅当模型文字小于500token，并最后一行小于500
            boolean isN1106All = false;//当出现gpt时，一定调用1106
            int index = 0;
            int allLength = 0;
            int msgLength = 0;
            System.out.println(messages.size());
            if(messages.size()>5){
                System.out.println("上下文超长限制");
                messages = putNewMsg(messages,4);
            }
            for (Object message : messages) {

                index = index+1;
                JSONObject data =JSONObject.parseObject(JSONObject.toJSONString(message)) ;
                if(index == messages.size()){
                    msgLength  =  String_length(data.getString("content"));
                }

                if(index==1){

                    if("system".equals(data.getString("role"))  ){
                        data.put("content", data.getString("content")+gpt4 );
                        newMsg.add(data);
                        continue;
                    }
                }

                String content = data.getString("content");
                if(content.contains("gpt")||content.contains("GPT")||content.contains("知识库")||content.contains("2023")){
                    isN1106All=true;
                }else{
                    int length =  String_length(content);
                    allLength+=length;
                    if(length>350){
                        isN1106 = false;
                    }
                }

                newMsg.add(message);
            }
            messages = newMsg;
            if(msgLength>3500){
                System.out.println("单次长度超限3500");
                messages = putNewMsg(messages,1);
            }
            if(msgLength>2000){
                System.out.println("单次长度超限2000");
                messages = putNewMsg(messages,2);
            }
            if(allLength>2000&&messages.size()>3){
                System.out.println("token总超长限制2000");
                messages = putNewMsg(messages,3);
            }
            if(allLength>4000&&messages.size()>2){
                System.out.println("token超长限制4000");
                messages = putNewMsg(messages,2);
            }

            System.out.println(DateUtil.now()+":"+JSONObject.toJSONString(messages.get(messages.size()-1)));
            jsonObject.put("messages", messages);
           // System.out.println("调用完成：耗时(s)：" + timer.intervalMs() * 1.0 / 1000);
            boolean isN = isN1106All ? true : isN1106;

            if(!authorization.contains("1chat")){
               return;
            }
            if(authorization.contains("ALL")){
                isN = true;
            }
            GPTOKStreamUtil.sendMsg4Nine(isN,authorization,jsonObject, response);
            if(isN){
                putRedis("ApiCount_"+DateUtil.today()+"_1106");
            }else {
                putRedis("ApiCount_"+DateUtil.today()+"_No1106");
            }

        } catch (Exception e)
        {

            System.out.println("error"+e.getMessage());
            response.getWriter().close();
        }
        //return null;
    }



    private static int String_length(String value) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }
    JSONArray  putNewMsg(JSONArray msg,int size){
            JSONArray newMsg = new JSONArray();
            if(msg.size()>size){
                newMsg.add(msg.get(0));
                newMsg.addAll(msg.subList(msg.size()-size,msg.size()));
                return newMsg;
            }else {
                return msg;
            }
   }

        void  putRedis(String redisKey){
            if(redisCacheService.hasKey(redisKey)){
                String countStr =  (String) redisCacheService.get(redisKey);
                Integer count = Integer.valueOf(countStr)+1;
                redisCacheService.setNoDel(redisKey,count.toString());
            }else{
                redisCacheService.setNoDel(redisKey,"1");
            }
            System.out.println(redisKey+":"+redisCacheService.get(redisKey));
        }
//    @GetMapping("/sse")
//    @CrossOrigin
//    public SseEmitter sseEmitter(String prompt, HttpServletResponse response) {
//        //国内需要代理 国外不需要
//
//        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
//                .timeout(600)
//                .apiKey("sk-tNsj6ZGuhcNzfy9q235a71C80c044aAaB4F25a69C6F971E3")
//                .apiHost("https://one-api.bltcy.top/")
//                .build()
//                .init();
//
//        SseEmitter sseEmitter = new SseEmitter(-1L);
//
//        SseStreamListener listener = new SseStreamListener(sseEmitter);
//        Message message = Message.of(prompt);
//
//        listener.setOnComplate(msg -> {
//            //回答完成，可以做一些事情
//            //回答完毕后关闭 SseEmitter
//            System.out.println("001");
//            sseEmitter.complete(); // 关闭 SseEmitter
//        });
//        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);
//
//        return sseEmitter;
//    }
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
