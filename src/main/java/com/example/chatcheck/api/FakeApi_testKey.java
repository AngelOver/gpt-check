package com.example.chatcheck.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.chatcheck.util.GPT3ClientUtil;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class FakeApi_testKey {

    //连接数据库
    //读取数据库中的账号密码
    //fake登录接口，获取token
    // token转api

//    static String login_url = "http://193.218.201.102:9049/auth/login";

//    http://101.34.206.59/
  //  static String login_url = "https://chat.zhile.io/auth/login?v=5";
    static String login_url = "https://chatproxy.xyhelper.cn/getsession";
    //    static String login_url = "http://193.218.201.102:9049/auth/login";
    static String fk_url = "https://ai.fakeopen.com/token/register";
    static String pk_url = "https://ai.fakeopen.com/pool/update";

    static String fk_api = "https://ai.fakeopen.com";
    static String testPK = "pk-MemLPyQZay_BiptXmc5idI-uBK-MYePIctbEptvMynQ";

    static List<String> tokenList = new ArrayList<>();

   static String getOneToken(String username, String password){
       // 设置请求URL和参数
       String url = login_url;
       HttpRequest request = HttpRequest.post(url)
               .header("Content-Type", "application/json");
       ;
       // 设置请求体参数


       JSONObject jsonObject = new JSONObject();
       jsonObject.put("username", username);
       jsonObject.put("password", password);

       request.body(jsonObject.toJSONString());

       // 发送请求并获取响应
       HttpResponse response = request.execute();
       String body = response.body();
       if(body.contains("accessToken")){
           JSONObject jsonObject1 = JSONObject.parseObject(body);
           String token = jsonObject1.getString("accessToken");
           return token;
       }
       System.out.println(body);
       return  null;
    }

    static String getOneSk(String name, String token){
        // 设置请求URL和参数
        String url = fk_url;
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        ;
//        request.body("{\"unique_name\":\""+name+"\",\"access_token\":\""+token+"\",\"expires_in\":0,\"site_limit\":\"\",\"show_conversations\":true}")
        // 设置请求体参数
        String value = "11chatcc-" + name;
        if(value.length()>40){
            value = value.substring(0, 40);
        }
        request.form("unique_name", value);
        request.form("access_token", token);
        request.form("expires_in", 0);
        request.form("site_limit", "");
        request.form("show_conversations", true);
        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String body = response.body();
        //System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
//        String token = response.body();
        return  jsonObject.getString("token_key");
    }

    static String getOnePk(List<String> keys, String pk){
        // 设置请求URL和参数
        String url = pk_url;
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        ;
//        request.body("{\"unique_name\":\""+name+"\",\"access_token\":\""+token+"\",\"expires_in\":0,\"site_limit\":\"\",\"show_conversations\":true}")
        // 设置请求体参数
        request.form("share_tokens", String.join("\n", keys));
        request.form("pool_token", pk);

        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String body = response.body();
        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
//        String token = response.body();
        return  jsonObject.getString("pool_token");
    }

    /**
     *
     * wileaston@eventbooking.shop-----pEtH#a76#3-----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
     * oscsullivan6@vanilla-soft.shop-----z8$B#Rm7$z-----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
     * bettyingram3@casst.best-----#pPQ$rFe28-----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
     * eburford6@edukom.shop-----68MUHvb@e4-----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
     * betshunter7@djg-montage.shop-----mPa@#dTJ5$-----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
     * @param args
     */
    public static void main22(String[] args) {

       String username  ="mayflower7182@gmail.com";
       String password  ="Tinker2013";
        String username2  ="genkidutheking@gmail.com";
        String password2  ="nero1234@e4";
        String oneToken = getOneToken(username, password);
        System.out.println(oneToken);
        String oneSk = getOneSk(username, oneToken);
        String oneSk2 = getOneSk(username2, getOneToken(username2, password2));



        String onePk = getOnePk(Arrays.asList(oneSk, oneSk2), testPK);
        System.out.println(onePk);
        System.out.println(oneSk);
       System.out.println(oneSk2);
       System.out.println(onePk);

        System.out.println(GPT3ClientUtil.testM3(fk_api, oneSk));
        System.out.println(GPT3ClientUtil.testM3(fk_api, oneSk2));

    }

    public static void main2(String[] args) {
        String username  ="dharmon3@casts.site";
        String password  ="ax@u@ZQ4H3";
        String oneToken = getOneToken(username, password);
        String res = testToken4(username, oneToken);
        if(res.contains("ou have sent too many messages to the mode")
                ||(res.contains("是")&&!res.contains("不是"))){
            System.out.println("gpt4==="+username);
        }
    }



    static  boolean testM4(String username,String password){
        try {
            String oneToken = getOneToken(username, password);
            if(oneToken==null){
                return false;
            }
            String oneSk = getOneSk(username, oneToken);
            String res = GPT3ClientUtil.testM4(fk_api, oneSk);
            if(res.contains("ou have sent too many messages to the mode")
                    ||(res.contains("是")&&!res.contains("不是"))){
                System.out.println("gpt4==="+username);
                return true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public static void main222(String[] args) {
        System.out.println(GPT3ClientUtil.testM3(fk_api, "fk-i2Y63GObdOOWlFGem4q6ohJrww0wU09wD4hX69dVQ-Y"));
    }

    static  String testToken4(String username,String oneToken){
        String oneSk = getOneSk(username, oneToken);
        return GPT3ClientUtil.testM4(fk_api, oneSk);
    }

    public static void main(String[] args) {
        String keys =


                        "mikecalalay1970@yahoo.com----Cha@1970\n" +
                        "evandro.lira12344@gmail.com----@Evandro06\n" +
                        "sergio.montiel89@gmail.com----tigers898\n" +
                        "Rithvikmunagala21@gmail.com----Akrur777\n" +
                        "annikalee48@gmail.com----Annikalee10\n" +
                        "Taillow40@gmail.com----Blizzard123\n" +
                        "ycho0608@gmail.com----!Dbstjd12\n" +
                        "zzzszknana@gmail.com----nana6919\n" +
                        "rongyuwen49@gmail.com----2468qwas\n" +
                        "puckcenter@gmail.com----Zzw0130251222\n" +
                        "zhoujw20@gmail.com----Spring@1990\n" +
                        "radulescu@gmail.com----Ilive4love";
        List<String> key = CollUtil.newArrayList(keys.split("\n"));
        List<String> error = CollUtil.newArrayList();
        List<String> result = CollUtil.newArrayList();

        List<String> fks = key.stream().map(s -> {
            System.out.println(s);
            boolean b = false;
            try {
                 b = testM4(s.split("----")[0], s.split("----")[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(b){
                result.add(s);
                return s;
            }else {
                error.add(s);
            }
            ThreadUtil.sleep(2000);
            return null;
        }).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
        System.out.println(fks);
        System.out.println(error);
        System.out.println(result);
    }

    private static String getOneSk(String s) {
        try {
            String[] split = s.split("-----");
            return getOneSk(split[0], getOneToken(split[0], split[1]));
        }catch (Exception e){
            System.out.println("获取sk失败"+e.getMessage());
            return null;
        }

    }


    public static String sendMsg(){
        // 设置请求URL和参数
        String url = login_url;
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded");
        ;
        // 设置请求体参数
        request.form("username", "betshunter7@djg-montage.shop");
        request.form("password", "mPa@#dTJ5$");

        // 发送请求并获取响应
        HttpResponse response = request.execute();
        System.out.println(response.getCookie("access-token"));
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
        // 打印响应结果
//        System.out.println(apiKey);
        System.out.println(responseBody);
        return responseBody;
    }


}
