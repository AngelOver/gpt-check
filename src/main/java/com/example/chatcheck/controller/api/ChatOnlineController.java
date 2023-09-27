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
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 设备列表对外接口，对其他模块
 */
@RestController
@RequestMapping("/v1/online")
public class ChatOnlineController {

    @Autowired
    private RedisCacheService redisCacheService;




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
        if(ObjectUtil.equals(msgId,clientId)){
            return returnMsg; //下发问题
        }
        if(StrUtil.isNotEmpty(msg)){
            msg=msg.replace("poe"," ");
            msg=msg.replace("perplexity"," ");
            msg=msg.replace("Perplexity"," ");
            msg=msg.replace("Poe"," ");
            msg=msg.replace("..."," ");
            msg=msg.replace("Waiting...","");
            msg=msg.replace("GPT-4 did not respond.","");
        }
        // 客户端只负责把id和内容上报，不做处理
        if(StrUtil.isNotEmpty(msgId)&&StrUtil.isNotEmpty(msg)){
            redisCacheService.set(msgId,msg);
           // System.out.println("客户端上报："+msgId+" "+msg);
        }

        if(StrUtil.isNotEmpty(msgId)&&req.isAllTextFlag()){
            if(!redisCacheService.hasKey(msgId+"ok")){
                redisCacheService.set(msgId+"ok",msg);
            }
//            if(redisCacheService.hasKey(req.clientId)){
//                redisCacheService.delete(req.clientId);
//            }
        }
//        if(req.freeFlag&&req.isAllTextFlag()){
//            //客户端已经释放，请下发任务
//            if(redisCacheService.hasKey(req.clientId)){
//                System.out.println("客户端已经释放，请下发任务");
//                redisCacheService.delete(req.clientId);
//            }
//        }
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

        //如果客户端上报，则盘点客户端存活
        if(!redisCacheService.hasKey("hasClient:"+clientId)){
           redisCacheService.set("hasClient:"+clientId,clientId);
        }

        if(redisCacheService.hasKey(clientId)){
        //    System.out.println(clientId+"服务端认为客户端 忙碌中======");
            return R.er("客户端忙碌中");
        }
//        if(!req.freeFlag){
//         //   System.out.println(clientId+"客户端 忙碌中======");
//            return R.er("客户端忙碌中");
//        }
        // 客户端空闲，从队列中取出任务
        if(MsgUtil.msgMap.size()==0){
           // System.out.println(clientId+"客户端 无任务 等待中======");
            return R.er("客户端 无任务 等待中");
        }
        MsgDTO peek = MsgUtil.msgMap.values().stream().collect(Collectors.toList()).get(0);
        MsgUtil.msgMap.remove(peek.getKey());
//        MsgDTO peek = JSONObject.parseObject((String)  redisCacheService.get(next),MsgDTO.class);
//        redisCacheService.delete(next);

        redisCacheService.set("dealing"+peek.key,clientId);//问题开始被客户端接管
        redisCacheService.set(peek.key,"");//任务开始响应
        redisCacheService.set(clientId,peek.key);//客户端开始占用
        redisCacheService.set(peek.key+"client",clientId);//问题开始被客户端接管
        System.out.println(clientId+"客户端 执行任务 msgId:"+peek.key+"======"+clientId);
        return R.ok(peek); //下发问题
    }


}
