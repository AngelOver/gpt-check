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

public class XYFakeApi {

    //连接数据库
    //读取数据库中的账号密码
    //fake登录接口，获取token
    // token转api

    static String login_url = "http://124.222.27.176:9049/auth/login";
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
        main3(args);

    }

    public static void main3(String[] args) {
        String keys = "zakosisuro398@pretreer.com       ----      6Ra6RXoUHB       ----      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\n" +
                "zatazux9172@pretreer.com       ----      n48oLBndth       ----      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\n" +
                "zavoyazute8786@pretreer.com       ----      mbr6o8LT0U       ----      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\n" +
                "zaziyekonacog6986@pretreer.com       ----      RO2O5E65PN       ----      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\n" +
                "zedasesodiqa4459@pretreer.com       ----      vOM7Q1eHpX       ----      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\n" +
                "zemenosequleboq5397@pretreer.com       ----      5XZKUw9f8b       ----      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\n" +
                "zidegokutuziw3510@pretreer.com       ----      m70f5ta26Z       ----      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\n" +
                "zikokoc2640@pretreer.com       ----      208V2hgdK0       ----      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\n" +
                "zimuhuxezaho6279@pretreer.com       ----      IcNbH9U9s9       ----      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\n" +
                "ziwabihig3345@pretreer.com       ----      BSkG8H4AC3       ----      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\n" +
                "zodoxipapariju7384@pretreer.com       ----      tz8cSx6w9A       ----      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\n" +
                "zugifaga2678@pretreer.com       ----      CH7s7TkEsX       ----      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\n" +
                "bacusidisuk8701@pretreer.com        ----   hOwYjc2df5        ----   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\n" +
                "badizamobexixej1486@exelica.com        ----   mLSY756445        ----   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\n" +
                "badosuq8564@exelica.com        ----   GcTJRD71R7        ----   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\n" +
                "bajomaze9658@pretreer.com        ----   tvP1d360wm        ----   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\n" +
                "bapazifopo6799@exelica.com        ----   96Ynzk72SB        ----   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\n" +
                "basarape1973@pretreer.com        ----   74MMY21yuQ        ----   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\n" +
                "baviqalirotepit4237@pretreer.com        ----   4j3WlC4EGz        ----   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\n" +
                "baxafahup9875@pretreer.com        ----   KQ3kBDTBno        ----   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\n" +
                "bayufu5922@exelica.com        ----   E2YIQS6ONA        ----   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\n" +
                "becapenaqor8438@exelica.com        ----   HNSJ8mSoKK        ----   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\n" +
                "becowuhiqehe3820@exelica.com        ----   130Z1JcyZ6        ----   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\n" +
                "befuli7451@pretreer.com        ----   0CHVNNNhMM        ----   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\n" +
                "bejujelu2065@pretreer.com        ----   xpOQN2Rjpz        ----   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\n" +
                "bejumasawis558@exelica.com        ----   wr44qekybY        ----   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\n" +
                "bekutozavog11@pretreer.com        ----   H1pBvD6hv9        ----   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\n" +
                "belacefit3109@pretreer.com        ----   1jiKxs42Hk        ----   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\n" +
                "bemitokibado3695@exelica.com        ----   H9f9zWu5LF        ----   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\n" +
                "beyori5426@pretreer.com        ----   27K5t4N0Vl        ----   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\n" +
                "bibilofipejuku1652@exelica.com        ----   r3806j4S70        ----   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\n" +
                "bigegun613@pretreer.com        ----   lH3HI4F1E4        ----   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\n" +
                "bihodejihibehuk1628@exelica.com        ----   5u6sBNJQkX        ----   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\n" +
                "bijahomixav9476@exelica.com        ----   9iG709BXkE        ----   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\n" +
                "bijicali8568@pretreer.com        ----   W9bwermlMi        ----   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\n" +
                "bijojaveyumi3910@pretreer.com        ----   R9EMT5L6PH        ----   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\n" +
                "bikodeyekazarok4152@exelica.com        ----   HECNGVIDyt        ----   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\n" +
                "biqudimeviye6934@pretreer.com        ----   J9z0d053kz        ----   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\n" +
                "bisefapakakem7131@pretreer.com        ----   RP62bn0SgB        ----   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\n" +
                "boduwike7022@pretreer.com        ----   2IHAYtkFod        ----   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\n" +
                "bofepubiq3068@exelica.com        ----   c1mtV6eaT0        ----   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\n" +
                "bogowucehuzoc4866@pretreer.com        ----   fZ6EQo7xqq        ----   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\n" +
                "bojazokehivin475@exelica.com        ----   27ZwJyOYps        ----   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\n" +
                "bojiwasiq5973@pretreer.com        ----   vOhuO8zXb1        ----   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\n" +
                "bomicawevumolok7176@pretreer.com        ----   IqC5vC20aI        ----   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\n" +
                "bonaqaxexu8518@pretreer.com        ----   cbRM84Le4H        ----   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\n" +
                "boyoyokumoliqo3411@pretreer.com        ----   738GE4W21l        ----   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\n" +
                "bozamefa2335@pretreer.com        ----   eMw1vg9Box        ----   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\n" +
                "bubatiku95@pretreer.com        ----   5UFf6p3m6N        ----   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\n" +
                "budabujiwi9228@exelica.com        ----   3OLJdL3nSv        ----   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\n" +
                "bujuzozagan696@pretreer.com        ----   FAQxP2As4X        ----   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\n" +
                "bukesuqahuvi1354@exelica.com        ----   9UC6GpSizV        ----   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\n" +
                "burorulanuratu8073@pretreer.com        ----   9BV5W07e7I        ----   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\n" +
                "butaqenav3766@pretreer.com        ----   XBn2YWiqWi        ----   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\n" +
                "cabeburitiven4972@pretreer.com        ----   Wj04IkRn0D        ----   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\n" +
                "cacujozaqobuti383@exelica.com        ----   2Bjqz8CSVV        ----   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\n" +
                "camasegagaki6097@pretreer.com        ----   07MLF5yf87        ----   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\n" +
                "camuyotala2996@pretreer.com        ----   wPUMnZygVh        ----   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\n" +
                "caqexubuse6384@pretreer.com        ----   Jqm2oAWOEf        ----   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\n" +
                "carozez8354@pretreer.com        ----   D11f7t0WF3        ----   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\n" +
                "casopajam7079@exelica.com        ----   XZ4nh0Ol3I        ----   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\n" +
                "cavohaxipapoji1097@exelica.com        ----   0q3w5q1szj        ----   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\n" +
                "cazolecawiteci9733@pretreer.com        ----   m009W3e22n        ----   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\n" +
                "cecibufi7942@pretreer.com        ----   a8AZ6r30nG        ----   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\n" +
                "cecutiqoheniho9000@pretreer.com        ----   PmKCEEhkRZ        ----   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\n" +
                "cedaye5796@pretreer.com        ----   SIiN6j14tK        ----   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\n" +
                "cediverogim7985@exelica.com        ----   1c9211I8kw        ----   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\n" +
                "cedulodunakavo4451@pretreer.com        ----   3Kzg5397aD        ----   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\n" +
                "cekuzek1938@exelica.com        ----   U90ImIgSN3        ----   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\n" +
                "cepomijoweneg3135@pretreer.com        ----   ToGBNr5lEh        ----   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\n" +
                "cequdayotawaj7249@pretreer.com        ----   ninQy41XKs        ----   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\n" +
                "cetomenubumi1391@pretreer.com        ----   naTYoSBp9e        ----   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\n" +
                "cevafu7517@pretreer.com        ----   56Cnk1v4nV        ----   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\n" +
                "cexovojeqalu3611@exelica.com        ----   u0F9W8gwUA        ----   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\n" +
                "ceyikixosate2260@pretreer.com        ----   1Q6dv18O35        ----   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\n" +
                "ciconuqi6062@pretreer.com        ----   jpAOboY2Mc        ----   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\n" +
                "cidetojepu7598@exelica.com        ----   rJR1scyjOT        ----   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\n" +
                "ciguheje7761@pretreer.com        ----   Qh7YaNaUQs        ----   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\n" +
                "cihosetu9374@exelica.com        ----   Sx3eiVi4Zp        ----   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\n" +
                "cikuciv2648@exelica.com        ----   13jv0r21sw        ----   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\n" +
                "cimoxogupu4929@pretreer.com        ----   juk9i5EO0O        ----   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\n" +
                "cisidohotuveta1186@pretreer.com        ----   12f9yhAaIE        ----   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\n" +
                "cixuwigolonu1600@exelica.com        ----   aV8NNBju16        ----   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\n" +
                "ciyanewawayo6670@pretreer.com        ----   QZCVHVyDlh        ----   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\n" +
                "cizehomusocimaq1900@pretreer.com        ----   g8vPjr59wk        ----   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\n" +
                "cobebega2515@pretreer.com        ----   JrW9QcR6rJ        ----   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\n" +
                "cofoyufuhima2155@pretreer.com        ----   ZuYbY1V3vQ        ----   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\n" +
                "cogiwaxoda2151@exelica.com        ----   4rqGh44Xtm        ----   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\n" +
                "cojivutel3057@pretreer.com        ----   aRQ7miTw1j        ----   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\n" +
                "cokewafonuc9648@pretreer.com        ----   db8TKvo2f8        ----   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\n" +
                "cokukuqexeza6160@exelica.com        ----   zK4Nd2930M        ----   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\n" +
                "comijexiq7662@pretreer.com        ----   bkxHlvZXsk        ----   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\n" +
                "conuzanum6695@pretreer.com        ----   7m0NS2ulQs        ----   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\n" +
                "coqoxida9435@pretreer.com        ----   CcZrY6N5HP        ----   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\n" +
                "cotoxasicinis9802@pretreer.com        ----   aed6G8t47D        ----   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\n" +
                "covozuw1278@exelica.com        ----   G63q18RbS5        ----   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\n" +
                "coyiwoc8955@pretreer.com        ----   J8yGadyTag        ----   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\n" +
                "cubizureba1002@pretreer.com        ----   gguVqfustV        ----   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\n" +
                "cubomepoq3847@pretreer.com        ----   5JTMFzi5s0        ----   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\n" +
                "cucopahifimuvim3805@pretreer.com        ----   bcw87XSh5S        ----   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";
        List<String> key = CollUtil.newArrayList(keys.split("\n"));
        List<String> fks = key.stream().map(s -> {
//            ThreadUtil.sleep(1);
            return getOneSk(s);
        }).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
//        System.out.println(fks);
//        String onePk = getOnePk(fks, testPK);
//        System.out.println(onePk);
    }

    private static String getOneSk(String s) {
        try {
            String[] split = s.split("----");
            System.out.println(split[1].trim());
            return split[0];
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
