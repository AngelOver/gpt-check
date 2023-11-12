//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.chatcheck.util.stream;

import cn.hutool.core.util.StrUtil;
import java.util.Objects;
import java.util.function.Consumer;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStreamListener extends EventSourceListener {
    private static final Logger log = LoggerFactory.getLogger(AbstractStreamListener.class);
    protected String lastMessage = "";

    protected Consumer<String> onComplate = (s) -> {
    };

    public AbstractStreamListener() {
    }

    public abstract void onMsg(String var1);

    public abstract void onError(Throwable var1, String var2);

    public void onOpen(EventSource eventSource, Response response) {
    }

    public void onClosed(EventSource eventSource) {
        System.out.println("onClose");
    }

    public void onEvent(EventSource eventSource, String id, String type, String data) {

        this.onMsg(data);
        if (data.equals("[DONE]")) {
            onComplate.accept(lastMessage);
            return;
        }

    }

    public void onFailure(EventSource eventSource, Throwable throwable, Response response) {
        try {
            try {
                log.error("Stream connection error: {}", throwable);
                String responseText = "";
                if (Objects.nonNull(response)) {
                    responseText = response.body().string();
                }

                log.error("response：{}", responseText);
                String forbiddenText = "Your access was terminated due to violation of our policies";
                if (StrUtil.contains(responseText, forbiddenText)) {
                    log.error("Chat session has been terminated due to policy violation");
                    log.error("检测到号被封了");
                }

                String overloadedText = "That model is currently overloaded with other requests.";
                if (StrUtil.contains(responseText, overloadedText)) {
                    log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
                }

                this.onError(throwable, responseText);
            } catch (Exception var11) {
                log.warn("onFailure error:{}", var11);
            } finally {
                eventSource.cancel();
            }

        } catch (Throwable var13) {
            throw var13;
        }
    }

    public void setOnComplate(Consumer<String> onComplate) {
        this.onComplate = onComplate;
    }

    public Consumer<String> getOnComplate() {
        return this.onComplate;
    }
}
