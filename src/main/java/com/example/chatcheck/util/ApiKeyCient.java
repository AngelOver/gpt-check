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
    final static String url1106D = "https://chatapi2.a3r.top/v1/chat/completions";

    final static String url1106Z = "https://vip-api-aiearth-hk.zeabur.app/v1/chat/completions";


    final static String url1106F = "https://ftddnaue.cloud.sealos.io/v1/chat/completions";

    final static String url1106W = "https://vip.zzapi.life/v1/chat/completions";

    final static String url1106ALL = "http://86.38.218.194:9091/v1/chat/completions";

    final static String url1106A1R= "http://86.38.218.194:9091/v1/chat/completions";

    final static String url1106PLUS = "http://86.38.218.194:9091/v1/chat/completions";

    final static String url1106T = "https://fastan.a1r.cc/api/v1/chat/completions";
    final static String authorization1106_T = "Bearer fastgpt-mLRgsrLsLkGP1JbVYH0CpZpS";

    final static String authorization1106_PLUS = "Bearer sk-aKpMvxGOBkcqs1ZCE3D3Fe04A48d4eE88fAeC7F2786a74E9";

    final static String authorization1106_A1R = "Bearer sk-Mu1eLEbji1w5eVNO7b11547815Fb4f53884bAdF6C1D12591";

    final static String authorization1106_ALL = "Bearer sk-aKpMvxGOBkcqs1ZCE3D3Fe04A48d4eE88fAeC7F2786a74E9";

    final static String authorization1106_W = "Bearer sk-ZXWHbVXr6K6h92Dy47347aB0EaA843D5B75b018965Ce3138";


    final static String authorization1106_B = "Bearer sk-0BN87qyUC7yfYXkb74947b53A63d4b7fB94570271d92Ab20";
    final static String authorization1106_L = "Bearer sk-FV9CkzcFqfrs782E4316850eC341450aBc7f267290F02507";
    final static String authorization1106_G = "Bearer sk-jWdUodjap88wnJlz3c7a23Db72924cBfAaF885124e92Ea16";
    final static String authorization1106_J = "Bearer sk-NMq42sYX30XmOIfcF9E13dAe4c0a4a6a9334908877552a99";
    final static String authorization1106_D = "Bearer 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";

    final static String authorization1106_Z = "Bearer sk-WttdgDfLnXM0AKSgE196Cb66De9b4510Ae1f5a7e37DfD1B4";

    final static String authorization1106_F = "Bearer sk-v2Vqwzg6Sd77AR6zEeA0F059477c48C6AaAc5c0099615fFe";

    final static String[] A1rArr = {url1106A1R, authorization1106_A1R};

    String apiKey;

    String apiURL;

    String model;

    public static String[] toType(String key, boolean isN) {
//        if (isN) {
//            System.out.println("is1106");
//            key = key.replaceAll("LIU", "").replaceAll("ZER", "").replaceAll("DAW", "").replaceAll("BLT", "").replaceAll("JIE", "").replaceAll("GAO", "");
//            //isN为true时，是短模型，强制1105
//        } else {
//            //isN为false时，是长模型，建议逆向
//            key = key.replaceAll("2L", "").replaceAll("2Z", "").replaceAll("2D", "").replaceAll("2B", "").replaceAll("2J", "").replaceAll("2G", "");
//        }

        if (key.contains("A1R") || key.contains("A1R")) {
            System.out.println("2A-A1R");
            return A1rArr;
        } else if (key.contains("ZER") || key.contains("2Z")) {
            System.out.println("2Z1106");
            return new String[]{url1106Z, authorization1106_Z};
        }  else if (key.contains("FTD") || key.contains("2F")) {
            System.out.println("2F1106");
            return new String[]{url1106F, authorization1106_F};
        }  else if (key.contains("WF") || key.contains("2W")) {
            System.out.println("2W1106");
            return new String[]{url1106W, authorization1106_W};
        }
        else  if (key.contains("ALL") || key.contains("2A")) {
            System.out.println("2A-ALL");
            return new String[]{url1106ALL, authorization1106_ALL,"gpt-4-all"};
        }
        else  if (key.contains("PLUS") || key.contains("2P")) {
            System.out.println("2A-PLUS");
            return new String[]{url1106PLUS, authorization1106_PLUS,"gpt-4-plus"};
        }
        else  if (key.contains("TAN") || key.contains("2T")) {
            System.out.println("TAN");
            return new String[]{url1106T, authorization1106_T};
        }
        else  if (key.contains("BLT") || key.contains("2B")) {
            System.out.println("2B1106");
            return new String[]{url1106B, authorization1106_B};
        }else if (key.contains("JIE") || key.contains("2J")) {
            System.out.println("2J1106");
            return new String[]{url1106J, authorization1106_J};
        } else if (key.contains("GAO") || key.contains("2G")) {
            System.out.println("2G1106");
            return new String[]{url1106G, authorization1106_G};
        } else if (key.contains("DAW") || key.contains("2D")) {
            System.out.println("2D1106");
            return new String[]{url1106D, authorization1106_D};
        } else {
            System.out.println(key);
            System.out.println("2L1106");
            return new String[]{url1106L, authorization1106_L,"gpt-4"};
        }

    }


}
