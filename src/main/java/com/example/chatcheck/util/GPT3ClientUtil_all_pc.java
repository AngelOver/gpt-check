//package com.example.chatcheck.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.thread.ThreadUtil;
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.core.util.ReUtil;
//import cn.hutool.http.Header;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.http.HttpUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CompletionService;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.ExecutorService;
//
//public class GPT3ClientUtil_all_pc {
//    static int allCount = 0;
//    static int successCount = 0;
//    static int runCount = 0;
//    static String[] msgList =new String[]{
//            "hello","say hello","how are you","what","when","what",
//            "red", "blue", "green", "yellow", "orange",
//            "apple", "banana", "orange", "kiwi", "grape",
//            "January", "February", "March", "April", "May",
//            "football", "basketball", "volleyball", "tennis", "baseball",
//            "dog", "cat", "bird", "hamster", "rabbit",
//            "China", "USA", "Canada", "Japan", "UK",
//            "pizza", "burger", "sushi", "noodles", "rice"
//    };
//    public static String test(String apiKey){
//        try {
//            // 设置请求URL和参数
//            String url = "http://chatapi2.a3r.top/v1/chat/completions";
////        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17892));
//            // 设置请求头部信息
//            HttpRequest request = HttpRequest.post(url)
//                    .header(Header.AUTHORIZATION, "Bearer "+apiKey)
//                    .header(Header.CONTENT_TYPE, "application/json");
//            int randomKey = RandomUtil.randomInt(1000)%msgList.length;
//            String msg = CollUtil.newArrayList(msgList).get(randomKey);
//            // 设置请求体参数
//            String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Play a game, what I say, what you reply to. Starting now。Start: say "+msg+"\"}], \"stream\": false}";
//            request.body(body);
//            // 发送请求并获取响应
//            HttpResponse response = request.execute();
//            String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
//            System.out.println(apiKey);
//            System.out.println(responseBody);
//            // 打印响应结果
//            return responseBody;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String sendMsg(String apiKey,String msg){
//        // 设置请求URL和参数
//        String url = "http://chatapi2.a3r.top/v1/chat/completions";
////        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        // 设置请求头部信息
//        HttpRequest request = HttpRequest.post(url)
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
//        System.out.println(apiKey);
//        System.out.println(responseBody);
//        return responseBody;
//    }
//
//
//    public static void main(String[] args) {
//        String listContent = HttpUtil.get("https://poe.com");
////使用正则获取所有标题
//        List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
//        for (String title : titles) {
//            //打印标题
//            Console.log(title);
//        }
////        Task task = new Task() {
////            @Override
////            public void execute() {
////                new GPT3ClientUtil_all().testKeys();
////            }
////        };
////
////        // 每天8:00执行一次
////        String cron = "0 0 8 * * ?";
////        // 添加定时任务
////        CronUtil.schedule(cron, task);
////
////        new Thread(() -> {
////            // 启动定时器
////            CronUtil.start();
////        }).start();
////
////        // 在主线程中等待
////        while (true) {
////            try {
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
//
//    }
//
//    private  void testKeys() {
//       // String keyStr = "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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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"   ;
//        String keyStr = "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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f,aab6cc43-9bb1-41a3-bbc8-10f9de93be2f";
//        List<String> list = CollUtil.newArrayList(keyStr.split(","));
//        Set<String> res = CollUtil.newLinkedHashSet();
////        for (int i = 0; i <50 ; i++) {
////            list.add(keyStr);
////        }
//        allCount = list.size();
//        try {
//            checkKeys(list).forEach(s -> {
//                if (s!=null){
//                    res.add(s);
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("结束了，总共"+allCount+"个"+successCount+"个成功");
//
//        List<String> result = CollUtil.newLinkedList();
//        list.stream().forEach(s -> {
//            if (res.contains(s)&&!result.contains(s)){
//                result.add(s);
//            }
//        });
//        System.out.println(result.size());
//        System.out.println(String.join(",",result));
//
//    }
//
//
//    public  List<String> checkKeys(List<String> keys) {
//        List<Callable<String>> tasks = new ArrayList<>();
//        keys.forEach(key -> {
//            tasks.add(() -> {
//                String test = test(key);
//                runCount++;
//                if(!test.contains("\"error\"")||test.contains("Limit: 3 / min")||test.contains("currently overloaded with other requests")){
//                    successCount++;
//                    System.out.println("当前执行第"+runCount+"个，总共"+allCount+"个"+successCount+"个成功");
//                    return key;
//                }else {
//                    System.out.println(test);
//                    return null;
//                }
//            });
//        });
//        return executeTasks(tasks);
//    }
//    /**
//     * 批量执行任务
//     * @param tasks
//     * @param <T>
//     * @return
//     */
//    public  <T> List<T> executeTasks(List<Callable<T>> tasks)  {
//        List<T> results = new CopyOnWriteArrayList<>();
//        int size = tasks.size();
//        if(tasks.size()>50){
//            size = 50;
//        }
//        ExecutorService executorService = ThreadUtil.newExecutor(size);
//        CompletionService<T> completionService = ThreadUtil.newCompletionService(executorService);
//        try {
//
//
//            for (Callable<T> task : tasks) {
//                completionService.submit(task);
//            }
//
//           for (int i = 0; i < tasks.size(); i++) {
//               try {
//                   T result = completionService.take().get();
//                   results.add(result);
//               }catch (Exception e){
//                   e.printStackTrace();
//               }
//
//            }
////
////            List<CompletableFuture<T>> futures = tasks.stream()
////                    .map(task -> CompletableFuture.supplyAsync(() -> {
////                        try {
////                            return task.call();
////                        } catch (Exception e) {
////                            // 处理异常
////                            System.out.println("任务执行出错"+e.getMessage());
////                            return null;
////                        }
////                    }))
////                    .collect(Collectors.toList());
////            results = futures.stream()
////                    .map(CompletableFuture::join)
////                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            throw new RuntimeException("任务执行出错", e);
//        } finally {
//            executorService.shutdown();
//        }
//        return results;
//    }
//
//
//
//}