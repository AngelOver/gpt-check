package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class GPT3ClientUtil_all {
    static int allCount = 0;
    static int successCount = 0;
    static int runCount = 0;
    static String[] msgList =new String[]{
            "hello","say hello","how are you","what","when","what",
            "red", "blue", "green", "yellow", "orange",
            "apple", "banana", "orange", "kiwi", "grape",
            "January", "February", "March", "April", "May",
            "football", "basketball", "volleyball", "tennis", "baseball",
            "dog", "cat", "bird", "hamster", "rabbit",
            "China", "USA", "Canada", "Japan", "UK",
            "pizza", "burger", "sushi", "noodles", "rice"
    };
    public static String test(String apiKey){
        try {
            // 设置请求URL和参数
            String url = "https://apic.a1r.cc/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17892));
            // 设置请求头部信息
            HttpRequest request = HttpRequest.post(url)
                    .header(Header.AUTHORIZATION, "Bearer "+apiKey)
                    .header(Header.CONTENT_TYPE, "application/json");
            int randomKey = RandomUtil.randomInt(1000)%msgList.length;
            String msg = CollUtil.newArrayList(msgList).get(randomKey);
            // 设置请求体参数
            String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Play a game, what I say, what you reply to. Starting now。Start: say "+msg+"\"}], \"stream\": false}";
            request.body(body);
            // 发送请求并获取响应
            HttpResponse response = request.execute();
            String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
            System.out.println(apiKey);
            System.out.println(responseBody);
            // 打印响应结果
            return responseBody;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String sendMsg(String apiKey,String msg){
        // 设置请求URL和参数
        String url = "https://api.a1r.cc/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url)
                .header(Header.AUTHORIZATION, "Bearer "+apiKey)
                .header(Header.CONTENT_TYPE, "application/json");
        // 设置请求体参数
        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+msg+"\"}], \"stream\": false}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
        // 打印响应结果
        System.out.println(apiKey);
        System.out.println(responseBody);
        return responseBody;
    }


    public static void main(String[] args) {
        new GPT3ClientUtil_all().testKeys();
        System.out.println("结束");

//        Task task = new Task() {
//            @Override
//            public void execute() {
//                new GPT3ClientUtil_all().testKeys();
//            }
//        };
//
//        // 每天8:00执行一次
//        String cron = "0 0 8 * * ?";
//        // 添加定时任务
//        CronUtil.schedule(cron, task);
//
//        new Thread(() -> {
//            // 启动定时器
//            CronUtil.start();
//        }).start();
//
//        // 在主线程中等待
//        while (true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    private  void testKeys() {
       // String keyStr = "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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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"   ;
        String keyStr = "sk-fjsItKUuz9NHuBdf05C049B1216e4248Ad15981dEd8a8a22";
        List<String> list = CollUtil.newArrayList(keyStr.split(","));
        Set<String> res = CollUtil.newLinkedHashSet();
        for (int i = 0; i <1 ; i++) {
            list.add(keyStr);
        }
        allCount = list.size();
        try {
            checkKeys(list).forEach(s -> {

                if (s!=null){
                    res.add(s);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("结束了，总共"+allCount+"个"+successCount+"个成功");

        List<String> result = CollUtil.newLinkedList();
        list.stream().forEach(s -> {
            if (res.contains(s)&&!result.contains(s)){
                result.add(s);
            }
        });
        System.out.println(result.size());
        System.out.println(String.join(",",result));

    }


    public  List<String> checkKeys(List<String> keys) {
        List<Callable<String>> tasks = new ArrayList<>();
        keys.forEach(key -> {
            tasks.add(() -> {
                String test = test(key);
                runCount++;
                if(!test.contains("\"error\"")||test.contains("Limit: 3 / min")||test.contains("currently overloaded with other requests")){
                    successCount++;
                    System.out.println("当前执行第"+runCount+"个，总共"+allCount+"个"+successCount+"个成功");
                    return key;
                }else {
                    System.out.println(test);
                    return null;
                }
            });
        });
        return executeTasks(tasks);
    }
    /**
     * 批量执行任务
     * @param tasks
     * @param <T>
     * @return
     */
    public  <T> List<T> executeTasks(List<Callable<T>> tasks)  {
        List<T> results = new CopyOnWriteArrayList<>();
        int size = tasks.size();
        if(tasks.size()>100){
            size = 100;
        }
        ExecutorService executorService = ThreadUtil.newExecutor(size);
        CompletionService<T> completionService = ThreadUtil.newCompletionService(executorService);
        try {


            for (Callable<T> task : tasks) {
                completionService.submit(task);
            }

           for (int i = 0; i < tasks.size(); i++) {
               try {
                   T result = completionService.take().get();
                   results.add(result);
               }catch (Exception e){
                   e.printStackTrace();
               }

            }
//
//            List<CompletableFuture<T>> futures = tasks.stream()
//                    .map(task -> CompletableFuture.supplyAsync(() -> {
//                        try {
//                            return task.call();
//                        } catch (Exception e) {
//                            // 处理异常
//                            System.out.println("任务执行出错"+e.getMessage());
//                            return null;
//                        }
//                    }))
//                    .collect(Collectors.toList());
//            results = futures.stream()
//                    .map(CompletableFuture::join)
//                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("任务执行出错", e);
        } finally {
            executorService.shutdown();
        }
        return results;
    }



}