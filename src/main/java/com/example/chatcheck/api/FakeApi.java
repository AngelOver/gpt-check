package com.example.chatcheck.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeApi {

    //连接数据库
    //读取数据库中的账号密码
    //fake登录接口，获取token
    // token转api

    static String login_url = "http://193.218.201.102:9049/auth/login";
    static String fk_url = "https://ai.fakeopen.com/token/register";
    static String pk_url = "https://ai.fakeopen.com/pool/update";

    static String testPK = "pk-MemLPyQZay_BiptXmc5idI-uBK-MYePIctbEptvMynQ";

    static List<String> tokenList = new ArrayList<>();

   static String getOneToken(String username, String password){
       // 设置请求URL和参数
       String url = login_url;
       HttpRequest request = HttpRequest.post(url)
               .header("Content-Type", "application/x-www-form-urlencoded");
       ;
       // 设置请求体参数
       request.form("username", username);
       request.form("password", password);

       // 发送请求并获取响应
       HttpResponse response = request.execute();
       HttpCookie accessCookie = response.getCookie("access-token");
       String token = accessCookie.getValue();
       System.out.println(token);
       return  token;
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
        System.out.println(body);
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
    public static void main(String[] args) {
       String username  ="q4524f@spitve.com";
       String password  ="DJ2Tu9mk";
        String username2  ="eburford6@edukom.shop";
        String password2  ="68MUHvb@e4";
        String oneToken = getOneToken(username, password);
        System.out.println(oneToken);
        String oneSk = getOneSk(username, oneToken);
        String oneSk2 = getOneSk(username2, getOneToken(username2, password2));

        String onePk = getOnePk(Arrays.asList(oneSk, oneSk2), testPK);
     //   System.out.println(onePk);
        System.out.println(oneSk);
        // System.out.println(oneSk2);
        //System.out.println(onePk);

    }

    public static void main3(String[] args) {
        String keys = "robertogriffith8@telefonylacno.shop-----@4MB$d@#AX-----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\n" +
                "jmcknight5@edukom.shop-----CS#672Q#9k-----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\n" +
                "heathergrier7@naostrofashion.shop-----J$2HD5gn9$-----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\n" +
                "wrollins9@telefonylacno.shop-----Z$#2r#$uV@-----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\n" +
                "kylespieker2@erismann.shop-----v3@@mU2t6z-----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\n" +
                "brucedinwiddie7@mudryinvestor.site-----7T87p$8w@@-----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\n" +
                "morglim3@mahadina.best-----s@tD93$3E@-----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\n" +
                "gwilliams7@mahadina.site-----42@3kg8@qV-----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\n" +
                "josepgonsalves3@naostrofashion.shop-----3@398$rGpn-----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\n" +
                "waltebohannon9@telefonylacno.shop-----6#$64Ez@$G-----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\n" +
                "leroymiley8@vanilla-soft.shop-----AhV3@KP#Ya-----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\n" +
                "robertwirth@telefonylacno.shop-----3@@54Vx3uT-----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\n" +
                "lestermarshall2@investujmudro.site-----s3pN#TX62$-----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\n" +
                "carcrockett7@milanalbums.shop-----T5$9saKKD#-----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\n" +
                "someadows3@bodytracker.shop-----#2ms$jETME-----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\n" +
                "lilliawilliams3@nootropic.site-----8$BN9#$y4C-----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\n" +
                "donschafer9@vanilla-soft.shop-----MH$v#8#292-----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\n" +
                "rovereen6@uliciansky.shop-----WMQ99@a#5$-----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\n" +
                "marthasenter9@nootropic.site-----s@$26@KXUu-----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\n" +
                "douglaspettus2@luhan.site-----hCj6N$82@#-----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\n" +
                "ricfallon@milanalbums.shop-----#bdb7e#W2@-----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\n" +
                "hgreen5@nastenkovo.best-----2cQB3#9Yw3-----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\n" +
                "jamekelly9@tripyou.shop-----uprah5@@rA-----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\n" +
                "briahughes7@snove.best-----zd7$#kgeGa-----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\n" +
                "lalerner8@ceragemslovensko.shop-----p@u#R#S2@f-----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\n" +
                "cbrinkerhoff3@suseneplody.best-----KMgc6$qH3W-----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\n" +
                "bryafreeman8@moderator-zabavac.shop-----JqNj6##$ad-----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\n" +
                "leonaparker9@moderator-zabavac.shop-----@@tyDZ3My9-----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\n" +
                "debcolby5@laktacninebe.shop-----nx7@R64#2Q-----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\n" +
                "shachandler7@ceragemslovensko.shop-----QNy2$4$W#h-----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\n" +
                "dahall7@jtarin.shop-----y3#M6352jQ-----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\n" +
                "michaecanela7@vifonka.shop-----983UmE@$Gw-----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\n" +
                "daviddrennan6@moderator-zabavac.shop-----#@5zMP2yr4-----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\n" +
                "masmith5@top-kolobezky.best-----Vt$2$sunq$-----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\n" +
                "vonryan5@laktacninebe.shop-----5z#NY6E2yR-----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\n" +
                "rsalinas4@milanalbums.shop-----$@Q4@TCez4-----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\n" +
                "ronniemardis8@top-kolobezky.best-----R@5M7#Cgv@-----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\n" +
                "edwarclark@alexandramurincakova.best-----#5pcBp@R$#-----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\n" +
                "estedaniels9@iverzine.best-----z9$$xZ$6$@-----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\n" +
                "jeffreysims5@rimac-automobili.shop-----6E@cTu2e@J-----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\n" +
                "williampeachey4@tatralegal.shop-----8#F3EpG2f@-----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\n" +
                "migcole9@ceragemslovensko.shop-----dZ93$Z@RDx-----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\n" +
                "robbradley3@rajvin-eshop.shop-----b$yPXq7XZ6-----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\n" +
                "miltonlollis6@rimac-automobili.shop-----$c#cRPm4Xu-----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\n" +
                "kimberlysherwood3@alexandramurincakova.best-----$CeZ##3fuT-----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\n" +
                "dwallace8@tripyou.shop-----$2$79#6STt-----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\n" +
                "michhelzer@ceragemslovensko.shop-----At3g$@7aTf-----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\n" +
                "dougcook3@bonjourparis.shop-----$@554$3MTk-----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\n" +
                "bilacton4@suseneplody.best-----92$6HT#6#y-----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\n" +
                "raymonpolster6@shrikailasa.best-----#v5R52r#2C-----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\n" +
                "clydewilson6@moderator-zabavac.shop-----GvE#Z3Z3#@-----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\n" +
                "christielondon6@tripyou.shop-----xTjeeuS$$8-----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\n" +
                "robaez9@innovationstb.shop-----SZx#dSR$6p-----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\n" +
                "jamenapier4@alexandramurincakova.best-----5$MNtf@$r4-----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\n" +
                "rcox8@snove.best-----s3$#2Zpc@b-----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\n" +
                "josephgraves5@shrikailasa.best-----A3##fTb2u5-----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\n" +
                "michaeltomlinson6@nastenkovo.best-----E#5e2A$a@$-----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\n" +
                "dhamilton7@tripyou.shop-----4$Z7t77XTW-----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\n" +
                "lfrancis3@snove.best-----zz2$3S55@G-----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\n" +
                "marcallan7@bonjourparis.shop-----#@e$g7J$@7-----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\n" +
                "rbrown8@mamaoffice.shop-----32h@hd9GE4-----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\n" +
                "franbeavers5@laktacninebe.shop-----4fhw$@YQ3@-----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\n" +
                "amhurst4@innovationstb.shop-----@Qe7bR6@w7-----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\n" +
                "douglamelvin9@laktacninebe.shop-----na452H$$@5-----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\n" +
                "gemiller2@rajvin-eshop.shop-----Z5#b#$24t@-----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\n" +
                "williakennedy8@snove.best-----3p#VuHh@x@-----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\n" +
                "tmessier2@bonjourparis.shop-----@3e3D3$P@6-----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";
        List<String> key = CollUtil.newArrayList(keys.split("\n")).subList(0, 5);
        List<String> fks = key.stream().map(s -> {
            ThreadUtil.sleep(2000);
            return getOneSk(s);
        }).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
        System.out.println(fks);
        String onePk = getOnePk(fks, testPK);
        System.out.println(onePk);
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
