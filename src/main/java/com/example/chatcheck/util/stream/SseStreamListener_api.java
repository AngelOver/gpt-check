////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package com.example.chatcheck.util.stream;
//
//import okhttp3.Response;
//import okhttp3.sse.EventSource;
//import org.asynchttpclient.AsyncHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class SseStreamListener_api extends AbstractStreamListener {
//    private static final Logger log = LoggerFactory.getLogger(SseStreamListener_api.class);
//    final SseEmitter sseEmitter;
//
//    private HttpServletResponse response;
//
//    public void setResponse(HttpServletResponse response) {
//        this.response = response;
//    }
//
//    public void onOpen(EventSource eventSource, Response response) {
//    }
//
//    public void onClosed(EventSource eventSource) {
//
//    }
//
//    public void onMsg(String message) {
//       // System.out.println(message);
//        //System.out.println("1111");
//       // SseHelper.send(this.sseEmitter, message);
//        try {
//          //  System.out.println("11111111==== " + message );
////            if(message.contains("[DONE]")){
////                String[] split = message.split("data:");
////                int length =  split.length;
////                for (int i = 1; i < length; i++) {
////                 String msg = "data:"+split[i];
////                        //  System.out.println("02");
////                    //System.out.println("data:"+msg);
////                    response.getWriter().write(msg);
////                    response.getWriter().flush();
////                  msg = null;
////                 }
////                 split = null;
////                return;
////            }
////
////            if(message.contains("role\":\"assistant")){
////                String[] split = message.split("data:");
////                int length =  split.length;
////                for (int i = 1; i < length; i++) {
////                    String msg = "data:"+split[i];
////                    //  System.out.println("02");
////                    //System.out.println("data:"+msg);
////                    response.getWriter().write(msg);
////                    response.getWriter().flush();
////                    msg = null;
////                }
////                split = null;
////                return;
////            }
//            response.getWriter().write("data: " + message + "\n\n");
//            response.getWriter().flush();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//    public void onError(Throwable throwable, String response) {
//        SseHelper.complete(this.sseEmitter);
//    }
//
//    public SseStreamListener_api(SseEmitter sseEmitter) {
//        this.sseEmitter = sseEmitter;
//    }
//}
