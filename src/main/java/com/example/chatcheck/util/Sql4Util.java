package com.example.chatcheck.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.tree.Node;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql4Util {


    public static String sendMsg(String apiKey,String msg){
        // 设置请求URL和参数
        String url = "http://hekr.zuolin.com/evh/deviceManagement/checkItem/list";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求头部信息
        HttpRequest request = HttpRequest.post(url)
                .header("Cache-Control", "no-cache")
                .header("Host", "hekr.zuolin.com")
                .header("Origin", "http://hekr.zuolin.com")
                 .header("Content-Length", "763")
                .header("Content-Type", "multipart/form-data;boundary=----WebKitFormBoundaryvIlMiNvcBABZlOG2")
                .header("Cookie", "token=qoPemt5B7QUV0mk4fmNcuY1Q2gu4Jy5bv0u2jOtYSKenRgCO3RUc69lAZ9PYnRFZR7myredx-VLWLHIOF7XNmd677umUg_GU1MtiwUn4yZgcFdkQptWMFTBlabaN_Ke-; user_id=1082194; namespace_id=999885; SECKEY_ABVK=cORNNUTcBUQNZN5gRGBd5CCEZwrpNzTPG7gYsHgRaPg%3D; BMAP_SECKEY=qRq96m6O3P6Z5fBAj3TNCoXUi88OWmVJxs6I0pquNctzijDWxOEqT_M56WfA2Q6npK8kzzLKiEJIfTMcttW84tq9znAR5BtUvbvOnPj8DkBaTKpxt1_Yxox12qy7n3tMKvAsEHOqWJ6r1aX3XTFV-hTQELue8ijxRWeL-UZEswr_IcBvp6lwwUBWfK2emFfc");

        http://hekr.zuolin.com
//Host:hekr.zuolin.com
//Origin:http://hekr.zuolin.com
                ;

        // 设置请求体参数
        String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+msg+"\"}], \"stream\": false}";
        request.form("namespaceId", "999885");
        request.form("communityId", "240111044332064117");
        request.form("organizationId", "1084516");
        request.form("appId", "976085");
        request.form("categoryId", "999885");
        request.form("namespaceId", "2");
        request.form("pageSize", "20");
        request.form("pageAnchor", "1");


        // 发送请求并获取响应
        HttpResponse response = request.execute();
        String responseBody = IoUtil.read(response.bodyStream(), "UTF-8");
        // 打印响应结果
//        System.out.println(apiKey);
        System.out.println(responseBody);
        return responseBody;
    }


    public static List result = new ArrayList();

    public static void main(String[] args) {
        sendMsg(null,null);


    }

    public static void soutItem(JSONObject obj, String type){
        if("sql".equals(type)){
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(obj));
            JSONArray children = obj.getJSONArray("children");
            if(children!=null) {
                jsonObject.put("children", children.size());
            }else {
                jsonObject.put("children",0);
            }
            //System.out.println(jsonObject);
            result.add(jsonObject);
        }else {
            System.out.println(obj.get("name"));
        }

    }

    static List<Node> toList(Tree<Node> tree) {
        List<Node> nodeList = new ArrayList<>();
        for (Tree<Node> childTree : tree.getChildren()) {
            nodeList.addAll(toList(childTree));
        }
        return nodeList;
    }

}