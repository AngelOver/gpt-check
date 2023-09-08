package com.example.chatcheck.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.util.GPT3ClientUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 设备列表对外接口，对其他模块
 */
@RestController
@RequestMapping("/chat")
public class ChatController {


    public static List<String> API_KEYS =new ArrayList<>( Arrays.asList(
            "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",
            // "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",
            // "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",
            "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",
            "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"
    ));
    public static List<String> REMOVED_API_KEYS = new ArrayList<>();


    /**
     * 设备资源接口-对外接口-分页查询
     *
     * @return 所有数据
     */
    @GetMapping("/chat")
    public String chat(String msg) {

        TimeInterval timer = DateUtil.timer();
        try {
            System.out.println("接口调用["+ DateTime.now().toString()+"]:"+msg);
            System.out.println(API_KEYS);
            List<String> apiKeys = API_KEYS;
            timer.start();

            msg = msg.trim();
            String replyStr = null;
            while(apiKeys.size() > 0) {
                int randomIndex = new Random().nextInt(apiKeys.size());
                String apiKey = apiKeys.get(randomIndex);
                System.out.println("apiKey：" + apiKey);
                try {
                    String s = GPT3ClientUtil.sendMsg(apiKey, msg);
                    JSONObject reply = JSONObject.parseObject(s);
                    if (reply.keySet().contains("error")) {
                        apiKeys.remove(apiKey);
                        if (!REMOVED_API_KEYS.contains(apiKey)) {
                            REMOVED_API_KEYS.add(apiKey);
                        }
                        System.out.println("apiKey：" + apiKey + " 调用接口失败，错误信息：" + reply.getJSONObject("error").getString("message"));
                        continue;
                    }
                    replyStr = reply.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    System.out.println("ChatGPT:" + replyStr);
                    break;
                } catch (Exception e) {
                    System.out.println("调用接口失败，apiKey：" + apiKey + " 错误信息：" + e.getMessage());
                    apiKeys.remove(apiKey);
                    if (!REMOVED_API_KEYS.contains(apiKey)) {
                        REMOVED_API_KEYS.add(apiKey);
                    }
                }
            }
            if (replyStr == null) {
                return "调用接口失败，没有可用的apiKey";
            }
            // 将结果存入缓存中
            return replyStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "处理失败"+e.getMessage();
        } finally {
            System.out.println("调用完成：耗时(s)："+timer.intervalMs()*1.0/1000);
            System.out.println(API_KEYS);
            System.out.println(REMOVED_API_KEYS);
        }
    }




    String res3 = "{\n" +
            "    \"error\": {\n" +
            "        \"message\": \"You exceeded your current quota, please check your plan and billing details.\",\n" +
            "        \"type\": \"insufficient_quota\",\n" +
            "        \"param\": null,\n" +
            "        \"code\": null\n" +
            "    }\n" +
            "}\n";
String res  = "ChatGPT: 当一只小兔子正准备上床睡觉时，他的妈妈告诉了他一个故事。\n" +
        "“很久很久以前，有一个小男孩，他总是不想睡觉。他会藏在被子里玩游戏，或者偷偷看书，甚至在床上跳舞，直到很晚才去睡觉。”\n" +
        "“直到有一天，他的奶奶告诉他一个秘密。她说，如果你在睡觉前做些放松的事情，你就能更快地入睡。”\n" +
        "“所以小男孩开始尝试做一些放松的事情，如沉思、听轻柔的音乐，做冥想等等。很快，他就意识到这些方法真的很有效。”\n" +
        "“小男孩以后每晚都做些放松的事情，他也很快入睡了，并且每天都感到更加精神和充满活力。”\n" +
        "小兔子听了这个故事后，闭上眼睛，想象着自己正坐在田野上，听着鸟儿的歌唱，慢慢地进入了梦乡。 \n" +
        "然后，他做了一个美好的梦，直到早上醒来，充满活力地开始新的一天。";
    String rep3 = "周报时间：2021年X月X日至2021年X月X日\n" +
            "本周工作内容：\n" +
            "1、修改了3个关于前段页面无法展示的bug，分别是XXX、XXX和XXX。通过仔细查看代码和调试，最终找到了问题所在并解决了这些bug。这些修改极大地提高了用户体验和网站的稳定性。\n" +
            "2、与前端团队联调了2个模块，分别是XXX和XXX。在联调过程中，发现了一些前后端接口不匹配的问题，经过沟通和修改，最终成功解决了这些问题，并保证了模块的正常运行。\n" +
            "3、参加了公司举办的技术分享会，学习了最新的前端技术和开发经验，对自己的技术水平有了一定的提升。\n" +
            "4、在工作之余，参与了公司组织的团建活动，加强了团队之间的沟通和合作，增强了凝聚力和归属感。\n" +
            "下周工作计划：\n" +
            "1、继续与前端团队联调其他的模块，确保网站的正常运行和用户体验。\n" +
            "2、学习新的前端技术和开发经验，提高自己的技术水平。\n" +
            "3、完成上级交办的其他任务，并按时提交工作报告。\n" +
            "4、参与公司组织的其他活动，加强团队之间的联系和合作。";
}
