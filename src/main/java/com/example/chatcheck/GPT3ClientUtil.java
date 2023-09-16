//package com.example.chatcheck;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.cron.CronUtil;
//import cn.hutool.cron.task.Task;
//import cn.hutool.http.Header;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.List;
//
//public class GPT3ClientUtil {
//    static String[] msgList =new String[]{"hello","say hello","how are you","what","when","what"};
//    public static String test(String apiKey){
//        // 设置请求URL和参数
//        String url = "https://api.openai.com/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        // 设置请求头部信息
//        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
//                .header(Header.AUTHORIZATION, "Bearer "+apiKey)
//                .header(Header.CONTENT_TYPE, "application/json");
//        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
//        String msg = CollUtil.newArrayList(msgList).get(randomKey);
//        // 设置请求体参数
//        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+msg+"\"}], \"stream\": false}";
//        request.body(body);
//
//        // 发送请求并获取响应
//        HttpResponse response = request.execute();
//        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
//        // 打印响应结果
//        System.out.println(responseBody);
//        return responseBody;
//    }
//
//    public static String sendMsg(String apiKey,String msg){
//        // 设置请求URL和参数
//        String url = "https://api.openai.com/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        // 设置请求头部信息
//        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
//                .header(Header.AUTHORIZATION, "Bearer "+apiKey)
//                .header(Header.CONTENT_TYPE, "application/json");
//        // 设置请求体参数
//        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+msg+"\"}], \"stream\": false}";
//        request.body(body);
//
//        // 发送请求并获取响应
//        HttpResponse response = request.execute();
//        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
//        // 打印响应结果
//        System.out.println(responseBody);
//        return responseBody;
//    }
//
//
//    public static void main(String[] args) {
//        testKeys();
//
//            Task task = new Task() {
//                @Override
//                public void execute() {
//                        testKeys();
//                }
//            };
//
//            // 每天8:00执行一次
//            String cron = "0 0 8 * * ?";
//            // 添加定时任务
//            CronUtil.schedule(cron, task);
//
//            new Thread(() -> {
//                // 启动定时器
//                CronUtil.start();
//            }).start();
//
//            // 在主线程中等待
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//    }
//
//    private static void testKeys() {
//        String keys = "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,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 ,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 ,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 ,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";
//        List<String> list = CollUtil.newArrayList(keys.split(","));
//        list.stream().forEach(key -> {
//            try {
//                test(key.trim());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}