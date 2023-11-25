package com.example.chatcheck.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
public class GPTOK2StreamUtil {

    private static long timeout = 120;

    public OkHttpClient init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        return client.build();
    }

    public SseEmitter sendRes(String msg, HttpServletResponse response, String url1106B, String authorization1106_B) throws InterruptedException, IOException {
        // 设置请求URL和参数
        String body = msg;
        EventSource.Factory factory = EventSources.createFactory(init());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url1106B)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                        body))
                .header("Authorization",authorization1106_B )
                .build();
        CountDownLatch latch = new CountDownLatch(1);
        PrintWriter writer = response.getWriter();
        EventSource eventSource = factory.newEventSource(request, new EventSourceListener() {
            public void onOpen(EventSource eventSource, Response okResponse) {
                response.setContentType(okResponse.header("Content-Type"));
                response.setStatus(okResponse.code());
            }

            public void onClosed(EventSource eventSource) {
                System.out.println("onClose");
                latch.countDown();
                writer.close();
            }

            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    response.getWriter().write("data: " + data + "\n\n");
                    response.getWriter().flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    latch.countDown();
                    writer.close();
                }

            }
            @Override
            public void onFailure(EventSource eventSource, Throwable throwable, Response response) {
                writer.close();
                latch.countDown();
            }
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
        return null;
    }

    public  SseEmitter sendMsg4Nine(boolean isN, String apiKey, JSONObject obj, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        obj.put("model","gpt-4-1106-preview");
        String msg = JSONObject.toJSONString(obj);
        System.out.println(DateUtil.now()+":"+msg);
        String[] client = ApiKeyCient.toType(apiKey,isN);
         return sendRes(msg,response,client[0],client[1]);

    }






}
