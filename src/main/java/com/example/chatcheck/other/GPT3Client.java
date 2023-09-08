package com.example.chatcheck.other;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class GPT3Client {
    public static void main(String[] args) {

        try {
            // 启用系统代理
            System.setProperty("java.net.useSystemProxies", "true");

            // 获取系统默认代理选择器
            List<Proxy> proxies = ProxySelector.getDefault().select(new URI("http://www.google.com"));
            Proxy proxy = proxies.get(0);
            InetSocketAddress addr = (InetSocketAddress) proxy.address();


            // 设置请求URL和参数
            // 设置请求URL和参数
            String url = "https://api.openai.com/v1/chat/completions";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            // 设置请求头部信息
            con.setRequestProperty("Authorization", "Bearer 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");
            con.setRequestProperty("Content-Type", "application/json");

            // 设置请求体参数
            String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"你好，今天是几号\"}], \"stream\": false}";
            con.setDoOutput(true);
            con.getOutputStream().write(body.getBytes("UTF-8"));

            // 发送请求并获取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印响应结果
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}