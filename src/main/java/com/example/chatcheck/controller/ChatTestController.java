//package com.example.chatcheck.controller;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.date.TimeInterval;
//import cn.hutool.core.util.RandomUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.example.chatcheck.controller.dto.ChatMsg;
//import com.example.chatcheck.controller.dto.MsgUtil;
//import com.example.chatcheck.util.GPT3ClientUtil;
//import io.github.furstenheim.CopyDown;
//import io.github.furstenheim.Options;
//import io.github.furstenheim.OptionsBuilder;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
///**
// * 设备列表对外接口，对其他模块
// */
//@RestController
//@RequestMapping("/chatT")
//public class ChatTestController {
//
//    @Autowired
//    private RedisCacheService redisCacheService;
//
//
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
//
//    public static List<String> API_KEYS =new ArrayList<>( Arrays.asList(
//            "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",
//            // "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",
//            // "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",
//            "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",
//            "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"
//    ));
//    public static List<String> REMOVED_API_KEYS = new ArrayList<>();
//
//    String t1="你好，请你模拟 http接口，把你好的回答写在 { “msg”:\"你的回答\"}，你只回复 json ，不要多余解释，直接开始，你好";
//
//
//    public static List<MsgDTO> MSGList = new ArrayList<>();
//
//    /**
//     * 设备资源接口-对外接口-分页查询
//     *
//     * @return 所有数据
//     */
//    @GetMapping("/chat")
//    public String chat(String msg) {
//
//        TimeInterval timer = DateUtil.timer();
//        try {
//            System.out.println("接口调用["+ DateTime.now().toString()+"]:"+msg);
//            System.out.println(API_KEYS);
//            List<String> apiKeys = API_KEYS;
//            timer.start();
//
//            msg = msg.trim();
//            String replyStr = null;
//            while(apiKeys.size() > 0) {
//                int randomIndex = new Random().nextInt(apiKeys.size());
//                String apiKey = apiKeys.get(randomIndex);
//                System.out.println("apiKey：" + apiKey);
//                try {
//                    String s = GPT3ClientUtil.sendMsg(apiKey, msg);
//                    JSONObject reply = JSONObject.parseObject(s);
//                    if (reply.keySet().contains("error")) {
//                        apiKeys.remove(apiKey);
//                        if (!REMOVED_API_KEYS.contains(apiKey)) {
//                            REMOVED_API_KEYS.add(apiKey);
//                        }
//                        System.out.println("apiKey：" + apiKey + " 调用接口失败，错误信息：" + reply.getJSONObject("error").getString("message"));
//                        continue;
//                    }
//                    replyStr = reply.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
//                    System.out.println("ChatGPT:" + replyStr);
//                    break;
//                } catch (Exception e) {
//                    System.out.println("调用接口失败，apiKey：" + apiKey + " 错误信息：" + e.getMessage());
//                    apiKeys.remove(apiKey);
//                    if (!REMOVED_API_KEYS.contains(apiKey)) {
//                        REMOVED_API_KEYS.add(apiKey);
//                    }
//                }
//            }
//            if (replyStr == null) {
//                return "调用接口失败，没有可用的apiKey";
//            }
//            // 将结果存入缓存中
//            return replyStr;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "处理失败"+e.getMessage();
//        } finally {
//            System.out.println("调用完成：耗时(s)："+timer.intervalMs()*1.0/1000);
//            System.out.println(API_KEYS);
//            System.out.println(REMOVED_API_KEYS);
//        }
//    }
//
//    @GetMapping("/baidu")
//    public String baidu(String msg ) {
//        System.out.println(msg);
//        return "你好，我是小智，很高兴为您服务";
//    }
//
//    @PostMapping("/baidu")
//    public String baidu(@RequestBody JSONObject obj ) {
//        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
//        String msg = CollUtil.newArrayList(msgList).get(randomKey);
//        System.out.println(obj.getString("client")+"客户端定时上报");
//        System.out.println(obj.toJSONString());
//        String text = obj.getString("text");
//        String msgKey = "";
//        //监控端 空闲、处理中
//        // 检查到编号，并且开始回答了，就把回答的内容存入redis
//        if(text.contains("#)")){
//            msgKey = text.split("#\\)")[0].replace("(","");
//            redisCacheService.set(msgKey+"-answer",obj.toJSONString());
//            if(text.contains("【")&&!text.contains("】")){
//                redisCacheService.set(msgKey+"-answer",text);
//                msg= "任务中";
//                System.out.println(msg);
//                return msg;
//            }
//        }
//
//        if(MSGList.size()>0){
//            String messages =(String) MSGList.get(0).getValue();
//            msg = MsgUtil.putMsg(MSGList.get(0).key,messages);
//            redisCacheService.set(MSGList.get(0).key+"-answer",null);
//            redisCacheService.set("客户端001冷却","",5, TimeUnit.SECONDS);
//            MSGList.remove(0);
//            System.out.println("读取消息，返回客户端");
//        }
//
////        if(redisCacheService.hasKey("messages")){
////            String messages =(String) redisCacheService.get("messages");
////            msg = ChatMsg.putMsg(messages);
////        }
//        System.out.println(msg);
//        return msg;
//    }
//
//
//
//    @PostMapping("/v1/chat/completions")
//    public String chat(@RequestBody JSONObject param ) {
//
//        String msgKey = RandomUtil.simpleUUID().substring(0, 4);
//        TimeInterval timer = DateUtil.timer();
//
//        System.out.println("结束");
//        System.out.println("耗时(s)：{}"+timer.intervalMs()*1.0/1000);
//
//        JSONArray messages = param.getJSONArray("messages");
//        redisCacheService.set(msgKey,messages.toJSONString());
//        MsgDTO e1 = new MsgDTO(msgKey, messages.toJSONString());
//        e1.putClientMsg();
//        MSGList.add(e1);
//        System.out.println(redisCacheService.get("messages"));
//        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
//        String msg = CollUtil.newArrayList(msgList).get(randomKey);
//
//        while(true) {
//           double time =timer.intervalMs()*1.0/1000;
//            if(time>120){
//                break;
//            }
//            if(redisCacheService.hasKey(msgKey+"-ok")) {
//                String value = (String) redisCacheService.get("key1");
//                System.out.println(value);
//                break;
//            } else if (redisCacheService.hasKey(msgKey+"-answer"))  {
//                String value = (String) redisCacheService.get(msgKey+"-answer");
//                System.out.println("等待中=========");
//               if(value.contains("("+msgKey+"#)")){
//                   System.out.println("开始响应=========");
//                   System.out.println(value);
//                   if(value.contains("】")){
//                       msg = value;
//                       break;
//                   }
//               }
//
//            } else {
//                try {
//                    Thread.sleep(1000);  // 每秒查询一次
//                } catch(InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return msg;
//    }
//
//
//
//
//
//
//
//    public static void main(String[] args) {
//        String result = "<div></div><div class=\"ChatMessage_messageWrapper__GRRcV\"><div><div class=\"Message_row___ur0Y\"><div class=\"Message_botMessageBubble__CPGMI Message_widerMessage__SmSLi\" style=\"max-width: unset !important;\"><div class=\"Markdown_markdownContainer__UyYrv\"><div><div class=\"MarkdownCodeBlock_codeHeader__5MZpO\"><div class=\"MarkdownCodeBlock_languageName__trujB\">json</div><button class=\"Button_buttonBase__0QP_m Button_flat__1hj0f MarkdownCodeBlock_copyButton__nm6Dw\"><svg viewBox=\"0 0 24 24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M16.02 20.96H3.78c-.41 0-.75-.34-.75-.75V7.74c0-.41.34-.75.75-.75h7.87c.21 0 .39.08.53.22l4.37 4.37c.14.14.22.32.22.53v8.11c0 .4-.34.74-.75.74ZM4.53 19.47h10.75v-6.61h-3.62c-.41 0-.75-.34-.75-.75V8.48H4.53v10.99Z\"></path><path d=\"m20.74 7.63-4.37-4.37c-.14-.14-.36-.2-.53-.22H8.01c-.41 0-.75.34-.75.75V5.5h1.49v-.97h6.34v3.62c0 .41.34.75.75.75h3.62v8.19h-1.2v1.49h1.95c.41 0 .75-.34.75-.75V8.16c0-.21-.08-.4-.22-.53Z\"></path></svg> Copy</button></div><pre class=\"MarkdownCodeBlock_preTag__dDgT9\" style=\"display: block; overflow-x: auto; background: rgb(43, 43, 43); color: rgb(248, 248, 242); padding: 0.5em;\"><code class=\"MarkdownCodeBlock_codeTag__ipdCC\" style=\"white-space: pre;\"><span>{\\n</span><span>  </span><span class=\"hljs-attr\">\"msg\"</span><span>: </span><span style=\"color: rgb(171, 227, 56);\">\"是的，我可以帮你写一个Java版的冒泡排序。下面是代码：\\n</span><span style=\"color: rgb(171, 227, 56);\">\\n</span><span style=\"color: rgb(171, 227, 56);\">```java\\n</span><span style=\"color: rgb(171, 227, 56);\">public class BubbleSort {\\n</span><span style=\"color: rgb(171, 227, 56);\">  public static void main(String[] args) {\\n</span><span style=\"color: rgb(171, 227, 56);\">    int[] array = {4, 2, 9, 6, 23, 12, 34, 0, 1};\\n</span><span style=\"color: rgb(171, 227, 56);\">    bubbleSort(array);\\n</span><span style=\"color: rgb(171, 227, 56);\">    for(int i: array){\\n</span><span style=\"color: rgb(171, 227, 56);\">      System.out.print(i + \\\\\" \\\\\");\\n</span><span style=\"color: rgb(171, 227, 56);\">    }\\n</span><span style=\"color: rgb(171, 227, 56);\">  }\\n</span><span style=\"color: rgb(171, 227, 56);\">\\n</span><span style=\"color: rgb(171, 227, 56);\">  static void bubbleSort(int[] array) {\\n</span><span style=\"color: rgb(171, 227, 56);\">    int n = array.length;\\n</span><span style=\"color: rgb(171, 227, 56);\">    for (int i = 0; i &lt; n-1; i++) {\\n</span><span style=\"color: rgb(171, 227, 56);\">      for (int j = 0; j &lt; n-i-1; j++) {\\n</span><span style=\"color: rgb(171, 227, 56);\">        if (array[j] &gt; array[j+1]) {\\n</span><span style=\"color: rgb(171, 227, 56);\">           int temp = array[j];\\n</span><span style=\"color: rgb(171, 227, 56);\">           array[j] = array[j+1];\\n</span><span style=\"color: rgb(171, 227, 56);\">           array[j+1] = temp;\\n</span><span style=\"color: rgb(171, 227, 56);\">        }\\n</span><span style=\"color: rgb(171, 227, 56);\">      }\\n</span><span style=\"color: rgb(171, 227, 56);\">    }\\n</span><span style=\"color: rgb(171, 227, 56);\">  }\\n</span><span style=\"color: rgb(171, 227, 56);\">}\\n</span><span style=\"color: rgb(171, 227, 56);\"></span></code></pre></div>\\n<p>这段代码会把一个整数数组进行冒泡排序，之后打印出排序后的数组。\"<br>\\n}</p>\\n<div><div class=\"MarkdownCodeBlock_codeHeader__5MZpO\"><div class=\"MarkdownCodeBlock_languageName__trujB\"></div><button class=\"Button_buttonBase__0QP_m Button_flat__1hj0f MarkdownCodeBlock_copyButton__nm6Dw\"><svg viewBox=\"0 0 24 24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M16.02 20.96H3.78c-.41 0-.75-.34-.75-.75V7.74c0-.41.34-.75.75-.75h7.87c.21 0 .39.08.53.22l4.37 4.37c.14.14.22.32.22.53v8.11c0 .4-.34.74-.75.74ZM4.53 19.47h10.75v-6.61h-3.62c-.41 0-.75-.34-.75-.75V8.48H4.53v10.99Z\"></path><path d=\"m20.74 7.63-4.37-4.37c-.14-.14-.36-.2-.53-.22H8.01c-.41 0-.75.34-.75.75V5.5h1.49v-.97h6.34v3.62c0 .41.34.75.75.75h3.62v8.19h-1.2v1.49h1.95c.41 0 .75-.34.75-.75V8.16c0-.21-.08-.4-.22-.53Z\"></path></svg> Copy</button></div><pre class=\"MarkdownCodeBlock_preTag__dDgT9\" style=\"display: block; overflow-x: auto; background: rgb(43, 43, 43); color: rgb(248, 248, 242); padding: 0.5em;\"><code class=\"MarkdownCodeBlock_codeTag__ipdCC\" style=\"white-space: pre;\"><span></span></code></pre></div></div></div></div><div class=\"Message_botOptimisticFooter__aQiG9\"></div></div></div>";
//        OptionsBuilder optionsBuilder = OptionsBuilder.anOptions();
//        Options options = optionsBuilder.withBr("-")
//                // more options
//                .build();
//        CopyDown converter = new CopyDown(options);
//        String markdown = converter.convert(result);
//        System.out.println(markdown);
//
//        Document doc = Jsoup.parseBodyFragment(result);
//        String text = doc.text();
//        System.out.println(text);
//    }
//
//}
