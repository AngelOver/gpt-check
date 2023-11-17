package com.example.chatcheck.util;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.util.stream.SseStreamListener_api;
import com.example.chatcheck.util.stream.SseStreamListener_rep2;
import com.example.chatcheck.util.stream.SseStreamListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GPTOKStreamUtil {
    static String authorization = "Bearer sk-fkTm6gFFvTuafl3fFf2476A94d994503B4304eEf42C0C16e";

    static String authorization1106 = "Bearer sk-PT6c2L4CQsvSOpwtAaF5AeA4526d4f6c8a196dD9878dD398";
    static String authorization1106_B = "Bearer sk-0BN87qyUC7yfYXkb74947b53A63d4b7fB94570271d92Ab20";

    static String authorization1106_L = "Bearer sk-FV9CkzcFqfrs782E4316850eC341450aBc7f267290F02507";

    private OkHttpClient okHttpClient;
    private long timeout = 90;
    public static OkHttpClient init() {
        long timeout = 90;
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        return client.build();
    }

//    public static void main(String[] args) {
//        test();
//    }
//    public static String test(){
//        // 设置请求URL和参数
//        String url1106B = "https://one-api.bltcy.top/v1/chat/completions";
//        String body = "{\"model\":\"gpt-4-1106-preview\",\"messages\":[{\"role\":\"system\",\"content\":\"You are a helpful assistant.\"},{\"role\":\"user\",\"content\":\"你好\"}],\"stream\":true}";
//        SseEmitter sseEmitter = new SseEmitter(-1L);
//        SseStreamListener listener = new SseStreamListener(sseEmitter);
//        EventSource.Factory factory = EventSources.createFactory(init());
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url1106B)
//                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
//                        body))
//                .header("Authorization", authorization1106_B)
//                .build();
//        factory.newEventSource(request, listener);
//        System.out.println("001");
//        // 发送请求并获取响应
//        // 打印响应结果
//        return null;
//    }

    public static SseEmitter send(String msg){
        // 设置请求URL和参数
        String url1106B = "https://one-api.bltcy.top/v1/chat/completions";
        String body = msg;
        SseEmitter sseEmitter = new SseEmitter(-1L);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        EventSource.Factory factory = EventSources.createFactory(init());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url1106B)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                        body))
                .header("Authorization", authorization1106_B)
                .build();
        factory.newEventSource(request, listener);
        listener.setOnComplate(data -> {
            //回答完成，可以做一些事情
            //回答完毕后关闭 SseEmitter
            sseEmitter.complete(); // 关闭 SseEmitter
        });
        // 发送请求并获取响应
        // 打印响应结果
        return sseEmitter;
    }

    public static SseEmitter sendRes(String msg, HttpServletResponse response, String url1106B, String authorization1106_B) throws InterruptedException {
        // 设置请求URL和参数
        String body = msg;
        SseEmitter sseEmitter = new SseEmitter(-1L);
        SseStreamListener_rep2 listener = new SseStreamListener_rep2(sseEmitter);
        listener.setResponse(response);
        EventSource.Factory factory = EventSources.createFactory(init());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url1106B)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                        body))
                .header("Authorization",authorization1106_B )
                .build();
        EventSource eventSource = factory.newEventSource(request, listener);
        CountDownLatch latch = new CountDownLatch(1);  // 创建 CountDownLatch
        listener.setOnComplate(data -> {
            //回答完成，可以做一些事情
            //回答完毕后关闭 SseEmitter
            sseEmitter.complete(); // 关闭 SseEmitter
            latch.countDown();
        });

        try {
            latch.await(360,TimeUnit.SECONDS);   // 阻塞当前线程直到计数器归零
        } catch (InterruptedException e) {
            // handle exception
        }finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        // 发送请求并获取响应
        // 打印响应结果
        System.out.println("retrun");
        return sseEmitter;
    }


    public static SseEmitter sendResQQ(String msg, HttpServletResponse response, String url1106B, String authorization1106_B) throws InterruptedException {
        // 设置请求URL和参数
        String body = msg;
        SseEmitter sseEmitter = new SseEmitter(-1L);
        SseStreamListener_api listener = new SseStreamListener_api(sseEmitter);
        listener.setResponse(response);
        EventSource.Factory factory = EventSources.createFactory(init());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url1106B)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                        body))
                .header("Authorization",authorization1106_B )
                .build();
        EventSource eventSource = factory.newEventSource(request, listener);
        CountDownLatch latch = new CountDownLatch(1);  // 创建 CountDownLatch
        listener.setOnComplate(data -> {
            //回答完成，可以做一些事情
            //回答完毕后关闭 SseEmitter
            sseEmitter.complete(); // 关闭 SseEmitter
            latch.countDown();
        });

        try {
            latch.await(360,TimeUnit.SECONDS);   // 阻塞当前线程直到计数器归零
        } catch (InterruptedException e) {
            // handle exception
        }finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        // 发送请求并获取响应
        // 打印响应结果
        System.out.println("retrun");
        return sseEmitter;
    }




    public static SseEmitter sendMsg4Nine(boolean isN, String apiKey, JSONObject obj, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {

        String url = "https://api.onechat.fun/v1/chat/completions";
        String url1106 = "https://chatapi.onechat.fun/v1/chat/completions";
        String url1106L = "https://turboapi.chatify.me/v1/chat/completions";
        // String url1106 = "http://172.17.0.1:9092/v1/chat/completions";
        String url1106B = "https://one-api.bltcy.top/v1/chat/completions";
        if(isN){
            obj.put("model","gpt-4-1106-preview");
            String msg = JSONObject.toJSONString(obj);
           //   System.out.printf(msg);
            if(apiKey.contains("LIU")){
                System.out.println("LIU1106");
                return sendRes(msg,response,url1106L,authorization1106_L);
            }else if(apiKey.contains("BLT")) {
                System.out.println("BLT1106");
                return sendRes(msg, response, url1106B, authorization1106_B);
            }else {
                System.out.println("LIU1106");
                return sendRes(msg,response,url1106L,authorization1106_L);
            }
        }else {
            System.out.println("no1106");
            String msg = JSONObject.toJSONString(obj);
            sendResQQ(msg,response,url, GPTOKStreamUtil.authorization);
            // GPTPlusUtil.sendMsg4Util(msg,response,url,authorization);
            // send2(msg);
            return null;
        }

    }






}
