package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.tomcat.util.buf.UEncoder;
import org.asynchttpclient.*;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.proxy.ProxyType;

import javax.servlet.http.HttpServletResponse;
import java.beans.Encoder;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GPTPlusUtil {
    static String cookie = "__cf_bm=Ek481MNOM6hYDOBcfCOLqv45qhETK6ByhARw5YWQd7w-1681664435-0-ARhAdVlBk+4EgOU3UHSS2Aax/IEoswhn5Xw3uJRAMyTmAze0wivScVyo9NKLYtTKDzT7TtBxN9HUpVJaIh7cq/VvcB4l/Fb+17X62p59LNtCFXWMrkLmvsiQhPKS2vF32QCDZRo5YIAdHXtxKc1nRNJKg5rpqdHob9b8aNk6zaEl; _cfuvid=Z7zqfbu.RkQhnsSDzrgkM6B__.oVXm2VSnp0G2_Dce4-1681664432735-0-604800000; cf_clearance=Vj.zIH735GUI5PZv_IBWxhU6S42fcowUNztYrb79sBc-1681664426-0-1-fa1a5b65.9e90501b.ad1b0485-250; __Host-next-auth.csrf-token=b06b812507eb6af84bc31c9192ce899c7f8553e85adc78146f66c3a30c0ffd24%7C97b9b460ba93820a847d9ce951a60172d04c79ccedf1362e34a65d3d09ad9df5; __Secure-next-auth.callback-url=https%3A%2F%2Fchat.openai.com%2F; __Secure-next-auth.session-token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..s0JYHw0SFtAgTZtI.S-Pg-rRpjR5ncP3RKRJZXv8xdwrTd6jwp6V9luRj712joOlD9rIshA6L-bI8xyXpBJQ5xa13XqaK_jT7N_vXaNkKpQUTDzOgL36RUTrIkjllYSsi1h6NCeyQPxbdZiGe8lVMkB6lFwf6ORcb6zhVuWjVk2PxIC-SiznRoedSrFatqWYIgiHZqdobDHKIsX-Z93H5zl_E4FZSH2JyRztSEXF_51NLGVMmUZf2t0qz1l7JLjAt0PmvkOK4OkBLty6Du60ZrA9zDA9qSpBwXDwXHtwfcQGFcRMfNkyE5AmMa7hnMKrnRQLTb53zJPbipg5NY8xmB97phYV2JLNh6C9iffBYijQJjWEHUmbXFerykgQ5JY6Qd-MszngkURom-Wi_AmOCbVw84Fi2fFdx_iYjmkUCs-KAZ49XT8NPPvGBvjD4BCm3Qpunmt8ElLDByMrCXOqHv0yyMhhqcq52HZIaxK-7BqqvsBQLFyuVII6WMyDeMexbFtSfYN8e_stb28PSu8AE0HBDMWslvL8Ab1WrUK1vvZCGt4cyOSaYFAiphP5dMy8oY2vuP9cQ-5CCZ6D7llRM68cevfzQElHyt2JN8MoaeXHq6n8FMS2Z06n5i31FxJLAtpX2S5xdQyILgCJoLy9CFb0dzUXrxFfsWP4QerRM5VDozbIjVoNrbki1BhQvajFYCD9i65ALEz-0OkmEJTiYrU_nRsAe16ut13JLyIBj50sO4oFhAIOwJoLH88zEKD4adogru8jm3uo4Sam4D0yfl4_Ynf5W6znnCyzEpCkvRlFiUx7eBMglwW9cT1NiWGw3VQwM1u8nMM6tLn5QBpf3w9CPXkPMaR3Daa5XlRA356vRh8gGSrY7EeImJz4MzoZ1Q9XKNvzKJUGS9nOCkyPuFWs_s3bSMaPwZ3g-SQ7cl5A8mmwY650cSov1B4KB-zgzv1dgrifuOMn9YUa4y5D7BKqGz6tm4fNe7FEEY44eSQ-y5_a5R8JDQHgONWjiKuGL0JR-4O3lWaW-GyHlomD9cNivlRJa4QO-h2VcEEiJz4jqwnaG4EMzPXbksNPSz1UK-kAitE0sJxxU1bb2KW-h3yjwutcAB2sGYgzKpraj6E3Lr4DKhZV3S_7vQuMBC4tYFmz-mbWWwFN4KOjcfvGfXvix5DbKMoMNwD0sqPt5JPLGc_rEBjhxGQaLh-y-vGBwA3AbWtUC6TrXId6dWB80UvsFCjUz3UH2GEFZP7MfXky7JP0SkhMhg8AQnLGCmxWNvm1o9wskVmjCyokd4mXtBVOBVg3gHTqEraX9ItOScwSlEc3p7gLCybtBt_w5BlZrOMQ73tdcLFBYqZADLvoWNEF88B2UPXmwgrR1nLO4Aj0GU5bNIL9nJw3YwOfWFSZHClQ_tJyBEgTGXx7flEiMlVPUqWdWAJ2qg0qfCiF9M5eqs-kHq2XSgB_RvcK6I65HP-vF6HYehxmID39L2oq27L-A6EW0ROSDCjWM4dC9Ift10CB-L7G0cPuxkT46InBabNSGp9xVbu5_pCpSpKthBqkj1IAXWpxrxxc_nxekUKnYLwg6mafp8T_b8sNI1AP0yz-TEqkGoPSqYnHtTtW_AdwbHL67x7mxI1L-Kftt9XMsfZKn-Ty5IJ089QYE-LCH5sxdvp_nKJgrtNM7mq5qpGPyQGuZ2MW088p0yBvp151NCOwVHaNH2AU_h74dH4to9Qf9XrvbcBemxPXvd2JRiF_mF0fRTROrndEsOU6z-hv6aG2C-iY7asvdNj1YM9k8nA6rwfCJ8wfrCkdctB9dUoPAzEZTJjCHGhqUS1n1FPqSqJH58W-xV0AtlnR3imXZqBFsb7iVs16HoF1oXlegkUruYXn1rb21YbmDxP3iYj6xw-rWh-kc848eMvCeXAhQn1SlCcIjJtWNGGTt7qHGmL3G31xP_G_MD9uDPziWClOQha_cIGjrQmeqYUkvC9d-oa4Eawp2-geLW1snTFFCUkZMpahmWx-0oC2weNPfncnKFlUGWURU1Iu2ad9fiI4n1v_roB9TNiDzTRhXYd4jJ1f3LVrDCYe-TCOgWb4JKzNV1GqH6gctukiL8wuTTaF37hle_M1WYC16dMv77jULaUMB4IdIOoRsBzoXMSZBO-4WGWBz7f0NcJHXCfl5TMFJWTbkwELzdn4TeMRu-u6dTlE9Ab_2n89sBjfsGB116L6mnblvHNcvTWSrKvTKAeN51FGqAlN4FhW3EF5qyU50MkezmDL9mFHFNJg_L4eYFrpKqJUdIcAkliKTKyqVX4gqtKkmWiPNehDiGn4LS22h0gkTBvEu_Jjl3OqAi8NU729TzYt_CrTWMVOv5hnXecFPb2ShLo27l5iVzWeLj1rj6zoXoPI6i_b4y_oYJUSI7-mLqUoXcMxR4w2ODIAoCtrSM0va0ZIlAdPRdy5PtF3NEVDpkAnhiY7pslJaIGB8LdtE2qOUoF-x21D_5JAgImtG0Xgr5W7fRSEuf_0XcODQqP-6I7e1ovTZmHMGUTBuExpW1oVmraGSaSkZBviGA-HwvQawebrL8vpQnibNmOeOG0JNcjqqN_3VirNFWFrtDfJ04uy1g-h_aqb5uspl3-OV1AaGfnZccfxCK3PyELHT75k2WC6E.sWxiwtDJL4UFsag7b0h-iw; _puid=user-xNrTC12791kT1SWbXXTbGvW5:1681664480-F3labgfmBrOUNQURT6gAxHsxuxRRoPgNT33zRtxzCmo%3D; intercom-session-dgkjq2bp=aU13VjBzV3VNUTJGTGFvQXJZazNsL2drSmVEMVB4cmp5Y3lZM1RIa0hWTHhFbjlCK3dLa0gyekpvT0xyVE5mTS0tdFRwWTVUcVRnR2t2OXhVMUg1RHFTZz09--2696f3887ce81ffb1bc8f5768bac73d02eaa171e; intercom-device-id-dgkjq2bp=06e4f926-f182-4068-a447-87f4f7291c85";
    static String authorization = "Bearer sk-fkTm6gFFvTuafl3fFf2476A94d994503B4304eEf42C0C16e";

    static String authorization1106 = "Bearer sk-PT6c2L4CQsvSOpwtAaF5AeA4526d4f6c8a196dD9878dD398";
    static String authorization1106_B = "Bearer sk-0BN87qyUC7yfYXkb74947b53A63d4b7fB94570271d92Ab20";

    static String url = "https://api.onechat.fun/backend-api/conversation";
    static String[] msgList =new String[]{"hello","say hello","how are you","what","when","what"};
    public static String test(){
        // 设置请求URL和参数
        String url = "https://api.onechat.fun/v1/chat/conversations";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
                .header("authorization",authorization);
//                .header(Header.CONTENT_TYPE, "application/json");
        int randomKey = RandomUtil.randomInt(1000)%msgList.length;
        String msg = CollUtil.newArrayList(msgList).get(randomKey);
        // 设置请求体参数
        String body = "{\"action\":\"next\",\"messages\":[{\"id\":\"e33ad94d-074b-4157-8cc6-87999710c155\",\"author\":{\"role\":\"user\"},\"content\":{\"content_type\":\"text\",\"parts\":[\"你是\"]}}],\"conversation_id\":\"1d440aa4-7470-492e-b5d9-de138a40fc7f\",\"parent_message_id\":\"80f92b4f-4af0-4ce4-83d4-46543a81878d\",\"model\":\"text-davinci-002-render-sha\",\"timezone_offset_min\":-480}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse response = request.executeAsync();
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
        // 打印响应结果
        System.out.println(responseBody);
        return responseBody;
    }
    public static String sendMsg(String msg){
        // 设置请求URL和参数
        String url = "https://chat.openai.com/backend-api/conversation";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
                .header("authorization",authorization)
                .header("cookie",cookie)
                .header("content-type","application/json")
                .header("origin","https://chat.openai.com")
                .header("referer","https://chat.openai.com/");
        // 设置请求体参数
        String msgId = RandomUtil.randomUUID();
        String body = "{\"action\":\"next\",\"messages\":[{\"id\":\""+msgId+"\",\"author\":{\"role\":\"user\"},\"content\":{\"content_type\":\"text\",\"parts\":[\""+msg+"\"]}}],\"conversation_id\":\"1d440aa4-7470-492e-b5d9-de138a40fc7f\",\"parent_message_id\":\"80f92b4f-4af0-4ce4-83d4-46543a81878d\",\"model\":\"text-davinci-002-render-sha\",\"timezone_offset_min\":-480}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse response = request.executeAsync();
        byte[] responseBytes = response.bodyBytes();
        // 将"application/octet-stream"类型的响应体转换为"text/event-stream"类型的字符串
        String responseStr = new String(responseBytes);

        return responseStr;
    }


    public static void sendMsg(String msg, ResponseHandler handler) {
        // 设置请求URL和参数
        String url = "https://chat.openai.com/backend-api/conversation";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url).setProxy(proxy)
                .header("authorization",authorization)
                .header("cookie",cookie)
                .header("content-type","application/json")
                .header("origin","https://chat.openai.com")
                .header("referer","https://chat.openai.com/");
        // 设置请求体参数
        String msgId = RandomUtil.randomUUID();
        String body = "{\"action\":\"next\",\"messages\":[{\"id\":\""+msgId+"\",\"author\":{\"role\":\"user\"},\"content\":{\"content_type\":\"text\",\"parts\":[\""+msg+"\"]}}],\"conversation_id\":\"1d440aa4-7470-492e-b5d9-de138a40fc7f\",\"parent_message_id\":\"80f92b4f-4af0-4ce4-83d4-46543a81878d\",\"model\":\"text-davinci-002-render-sha\",\"timezone_offset_min\":-480}";
        request.body(body);

        // 发送请求并获取响应
        HttpResponse responseMsg = request.executeAsync();
        InputStream responseStream = responseMsg.bodyStream();

        LineHandler lineHandler = new LineHandler() {
            @Override
            public void handle(String line) {
                handler.handleLine(line);
            }
        };

        IoUtil.readLines(responseStream, Charset.forName("UTF-8"), lineHandler);
        try {
            responseStream.close();
        } catch (IOException e) {
            // ignore
        }
        handler.handleComplete();

    }



    public interface ResponseHandler {
        void handleLine(String line);
        void handleError(Throwable t);
        void handleComplete();
    }


    public static void sendMsg3(String msg, ResponseHandler handler) throws ExecutionException, InterruptedException {
        // 设置请求URL和参数
        String url = "https://apic.littlewheat.com/v1/chat/completions";
      //  InetSocketAddress proxyAddr = new InetSocketAddress("127.0.0.1", 7890);
        //  ProxyServer proxy = new ProxyServer.Builder("127.0.0.1", 7890).build();
        // 创建AsyncHttpClient实例
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        // 设置请求头部信息
        BoundRequestBuilder requestBuilder = client.preparePost(url)
                //.setProxyServer(proxy)
                .addHeader("authorization",authorization)
                .addHeader("cookie",cookie)
                .addHeader("content-type","application/json")
                .addHeader("origin","https://chat.openai.com")
                .addHeader("referer","https://chat.openai.com/");

        // 设置请求体参数
        String msgId = RandomUtil.randomUUID();
        String body =msg;
        requestBuilder.setBody(body);

        // 发送请求并获取响应
        requestBuilder.execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                InputStream responseStream = response.getResponseBodyAsStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        handler.handleLine(line);
                    }
                } catch (Throwable t) {
                    handler.handleError(t);
                } finally {
                    try {
                        responseStream.close();
                    } catch (IOException e) {
                        // ignore
                    }
                    handler.handleComplete();
                }
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                handler.handleError(t);
            }
        }).toCompletableFuture().get();
    }

    public static void sendMsg4Nine(boolean isN, JSONObject obj, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {

        String url = "https://api.onechat.fun/v1/chat/completions";
        String url1106 = "https://chatapi.onechat.fun/v1/chat/completions";
        // String url1106 = "http://172.17.0.1:9092/v1/chat/completions";
         String url1106B = "https://one-api.bltcy.top/v1/chat/completions";
        if(isN){
            obj.put("model","gpt-4-1106-preview");
            System.out.println("1106");
            String msg = JSONObject.toJSONString(obj);
            // sendMsg4Util(msg,response,url1106,authorization1106);
            sendMsg4Util(msg,response,url1106B,authorization1106_B);

            System.out.printf(msg);
        }else {
            System.out.println("no1106");
            String msg = JSONObject.toJSONString(obj);
            sendMsg4Util(msg,response,url,authorization);
        }

    }

    public static void sendMsg4(String msg, HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        String url = "https://api.onechat.fun/v1/chat/completions";
        sendMsg4Util(msg,response,url,authorization);
    }
        public static void sendMsg4Util(String msg, HttpServletResponse response,String url, String authorization) throws ExecutionException, InterruptedException, IOException {
        PrintWriter writer = response.getWriter();
        // 设置请求URL和参数
        //String url = "https://apic.littlewheat.com/v1/chat/completions";



        //https://api.onechat.fun
       // System.out.println(msg);
        // 创建AsyncHttpClient实例
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        // 设置请求头部信息
        Request request = new RequestBuilder("POST")
                .setUrl(url)
              //  .setProxyServer(proxy)
                .addHeader(Header.AUTHORIZATION.name(),authorization)
                .addHeader(Header.CONTENT_TYPE.name(),"application/json")
                .setReadTimeout(360000)
                .setRequestTimeout(360000)
                .setFollowRedirect(true)
//                .addHeader("cookie",cookie)
//                .addHeader("content-type","application/json")
//                .addHeader("origin","https://chat.openai.com")
//                .addHeader("referer","https://chat.openai.com/")
                .setBody(msg)
                .build();

        // 发送请求并获取响应
        client.executeRequest(request, new AsyncHandler<Void>() {
           // private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            private  int index = 0;

            @Override
            public State onStatusReceived(HttpResponseStatus status) throws Exception {
                response.setStatus(status.getStatusCode());
                return State.CONTINUE;
            }

            @Override
            public State onHeadersReceived(HttpHeaders headers) throws Exception {
                response.setContentType(headers.get("Content-Type"));
                return State.CONTINUE;
            }

            @Override
            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                //bytes.write(bodyPart.getBodyPartBytes());

                String s = new String(bodyPart.getBodyPartBytes(), StandardCharsets.UTF_8);
                // System.out.println("01");
                // System.out.println(s);
                if(index==0){
                    index =1;
                    if(s.contains("data:")){
                        String[] split = s.split("data:");
                        int length =  split.length;
                        if(length>2){
                            for (int i = 1; i < length; i++) {
                                String msg = "data:"+split[i];
                                //  System.out.println("02");
                                //System.out.println("data:"+msg);
                                writer.write(msg);
                                writer.flush();
                                msg = null;
                            }
                            split = null;
                            s=null;
                            return State.CONTINUE;
                        }
                    }
                }
                if(s.contains("[DONE]")){
                    String[] split = s.split("data:");
                    int length =  split.length;
                    if(length>2){
                        for (int i = 1; i < length; i++) {
                            String msg = "data:"+split[i];
                            //  System.out.println("02");
                            //System.out.println("data:"+msg);
                            writer.write(msg);
                            writer.flush();
                            msg = null;
                        }
                        split = null;
                        s=null;
                        return State.CONTINUE;
                    }
                }
               // System.out.println(new String(bodyPart.getBodyPartBytes(), StandardCharsets.UTF_8));
                writer.write(s);
                writer.flush();
                s=null;
//                if(new String(bodyPart.getBodyPartBytes()).contains("[DONE]")){
//                    response.getWriter().close();
//                }
                return State.CONTINUE;
            }

            @Override
            public Void onCompleted() throws Exception {
//                response.getOutputStream().write(bytes.toByteArray());
//                response.getOutputStream().flush();
               // response.getOutputStream().close();
                writer.close();
                return null;
            }

            @Override
            public void onThrowable(Throwable t) {
                t.printStackTrace();
            }
        }).toCompletableFuture().get();;
        client.isClosed();
            client.close();
    }








}
