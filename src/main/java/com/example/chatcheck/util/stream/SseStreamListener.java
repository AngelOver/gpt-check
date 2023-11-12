//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.chatcheck.util.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseStreamListener extends AbstractStreamListener {
    private static final Logger log = LoggerFactory.getLogger(SseStreamListener.class);
    final SseEmitter sseEmitter;

    public void onMsg(String message) {
       // System.out.println(message);
        //System.out.println("1111");
        SseHelper.send(this.sseEmitter, message);
    }

    public void onError(Throwable throwable, String response) {
        SseHelper.complete(this.sseEmitter);
    }

    public SseStreamListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }
}
