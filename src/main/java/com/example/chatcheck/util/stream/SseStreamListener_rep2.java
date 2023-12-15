////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package com.example.chatcheck.util.stream;
//
//import okhttp3.Response;
//import okhttp3.sse.EventSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class SseStreamListener_rep2 extends AbstractStreamListener {
//    private static final Logger log = LoggerFactory.getLogger(SseStreamListener_rep2.class);
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
//    public SseStreamListener_rep2(SseEmitter sseEmitter) {
//        this.sseEmitter = sseEmitter;
//    }
//}
