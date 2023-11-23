package com.example.chatcheck.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyCient {

    final static String url1106B = "https://one-api.bltcy.top/v1/chat/completions";
    final static String url1106L = "https://turboapi.chatify.me/v1/chat/completions";
    final static String url1106G = "https://api.ai-gaochao.cn/v1/chat/completions";
    final static String url1106J = "http://154.12.87.114:3000/v1/chat/completions";


    final  static String authorization1106_B = "Bearer sk-0BN87qyUC7yfYXkb74947b53A63d4b7fB94570271d92Ab20";
    final static String authorization1106_L = "Bearer sk-FV9CkzcFqfrs782E4316850eC341450aBc7f267290F02507";
    final static String authorization1106_G = "Bearer sk-jWdUodjap88wnJlz3c7a23Db72924cBfAaF885124e92Ea16";
    final static String authorization1106_J = "Bearer sk-NMq42sYX30XmOIfcF9E13dAe4c0a4a6a9334908877552a99";

    String apiKey;

    String apiURL;

    String model;

   public static String[] toType(String key){
        if(key.contains("LIU")||key.contains("2L")){
            System.out.println("2L1106");
            return new String[]{url1106L,authorization1106_L};
        }else if(key.contains("BLT")||key.contains("2B")) {
            System.out.println("2B1106");
            return new String[]{url1106B,authorization1106_B};
        }else if(key.contains("JIE")||key.contains("2J")) {
            System.out.println("2J1106");
            return new String[]{url1106J,authorization1106_J};
        }else {
            System.out.println("2G1106");
            return new String[]{url1106G,authorization1106_G};
        }

    }



}
