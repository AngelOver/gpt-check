package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.Arrays;
import java.util.List;

public class GPT3ClientUtil_mail {
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
        String keyStr = "" +
               "gpt邮箱：q2r2gorj9@ssttbb.cc   辅助邮箱：mhwtv1f7l@outlook.com  密码：t9mj6dk9a：sk quZOcBeogGduvpH8TaFDT3BlbkFJxt2ODtprZOdmXqVTKX8U"
                ;
        keyStr = keyStr.replaceAll("sk ",",sk-");
        List<String> list = CollUtil.newArrayList();
        Arrays.stream(keyStr.split("\\\n")).map(s -> s.split(",")).forEach(strings -> {
            System.out.println(":"+strings[0]);
            System.out.println("sk:"+strings[1]);
            list.add(strings[1].trim());
        });
        List<String> res = CollUtil.newArrayList();

  //     List<String> list = CollUtil.newArrayList(keys.split(","));
        int i = 1;
        int size = list.size();
        for (String key : list) {
            try {
                System.out.println("第"+i+"/"+size+"个key");
                String test = test(key.trim());

                if(!test.contains("\"error\"")||test.contains("Limit: 3 / min")){
                    res.add(Arrays.asList(keyStr.split("\\\n")).get(i - 1));
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("共"+res.size()+"个");
        for (String re : res) {
            System.out.println(re);
        }

    }
}