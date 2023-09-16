package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GPT3ClientUtil_testLimit {
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
        // 设置请求URL和参数
        String url = "http://chatapi2.a3r.top/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 17892));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url)
                .header(Header.AUTHORIZATION, "Bearer "+apiKey)
                .header(Header.CONTENT_TYPE, "application/json");
        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
        String msg = CollUtil.newArrayList(msgList).get(randomKey);
        // 设置请求体参数
        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+msg+"\"}], \"stream\": false}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
        // 打印响应结果
        System.out.println(msg);
        System.out.println(apiKey);
        System.out.println(responseBody);
        return responseBody;
    }

    public static String sendMsg(String apiKey,String msg){
        // 设置请求URL和参数
        String url = "http://chatapi2.a3r.top/v1/chat/completions";
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
        testKeys();

            Task task = new Task() {
                @Override
                public void execute() {
                        testKeys();
                }
            };

            // 每天8:00执行一次
            String cron = "0 0 8 * * ?";
            // 添加定时任务
            CronUtil.schedule(cron, task);

            new Thread(() -> {
                // 启动定时器
                CronUtil.start();
            }).start();

            // 在主线程中等待
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }

    private static void testKeys() {
        String keyStr = "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"   ;
        List<String> list = CollUtil.newArrayList(keyStr.split(","));
        List<String> res = CollUtil.newArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(keyStr);
        }

        checkKeys(list).forEach(s -> {
            if (s!=null){
                res.add(s);
            }
        });
        System.out.println(res.size());
        System.out.println(String.join(",",res));

    }


    public static List<String> checkKeys(List<String> keys) {
        List<Callable<String>> tasks = new ArrayList<>();
        keys.forEach(key -> {
            tasks.add(() -> {
                String test = test(key);
                if(!test.contains("\"error\"")||test.contains("Limit: 3 / min")){
                    return key;
                }else {
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
    public static <T> List<T> executeTasks(List<Callable<T>> tasks)  {
        List<T> results = new CopyOnWriteArrayList<>();
        ExecutorService executorService = ThreadUtil.newExecutor(10);
        CompletionService<T> completionService = ThreadUtil.newCompletionService(executorService);
        try {
            for (Callable<T> task : tasks) {
                completionService.submit(task);
            }
            for (int i = 0; i < tasks.size(); i++) {
                T result = completionService.take().get();
                results.add(result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("任务执行被中断", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("任务执行出错", e);
        } finally {
            executorService.shutdown();
        }
        return results;
    }
}