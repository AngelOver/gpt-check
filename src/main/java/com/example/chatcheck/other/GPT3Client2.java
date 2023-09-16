package com.example.chatcheck.other;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class GPT3Client2 {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {


        // 设置请求URL和参数
        String url = "https://api.openai.com/v1/chat/completions";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.82.13.50", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
                .header(Header.AUTHORIZATION, "Bearer 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")
                .header(Header.CONTENT_TYPE, "application/json");

        // 设置请求体参数
        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"你好，今天是几号\"}], \"stream\": false}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");

        // 打印响应结果
        System.out.println(responseBody);
    }
}