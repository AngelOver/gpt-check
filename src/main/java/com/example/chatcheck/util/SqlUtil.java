package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.lang.tree.Node;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.*;
import org.asynchttpclient.proxy.ProxyServer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil {



    public static void main(String[] args) {
        String fileContent = FileUtil.readUtf8String("D:\\soft\\Downloads\\prompt-root.json");
        JSONObject jsonObject = JSONObject.parseObject(fileContent);

        JSONArray children = jsonObject.getJSONArray("children");
        int i = 0;
        for (Object child : children) {
            JSONObject jsonObject2 = JSONObject.parseObject(JSON.toJSONString(child));
            JSONArray children2 = jsonObject2.getJSONArray("children");
            i++;
            for (Object child3 : children2) {

                JSONObject jsonObject3 = JSONObject.parseObject(JSON.toJSONString(child3));
                JSONArray children3 = jsonObject3.getJSONArray("data");
             //  System.out.println(jsonObject3.getString("category_name"));
                System.out.println("INSERT INTO `nice_littlewheat`.`ai_curses`( `title`, `en_title`, `cate_id`) VALUES ( '"+jsonObject3.getString("category_name") +"', '"+jsonObject3.getString("category_code") +"', "+i+");");

                for (Object child4 : children3) {

                    JSONObject jsonObject4 = JSONObject.parseObject(JSON.toJSONString(child4));
                    JSONArray children4 = jsonObject4.getJSONArray("");
            //        System.out.println(jsonObject4.getString("prompt_keyword")+i);
                    System.out.println("INSERT INTO `nice_littlewheat`.`ai_curses`( `title`, `en_title`, `cate_id`) VALUES ( '"+jsonObject4.getString("prompt_keyword") +"', '"+jsonObject4.getString("prompt_code") +"', "+i+");");
                }
            }
        }

        // 使用TreeUtil创建Tree对象

        // 将Tree对象转换为列表

        // 打印结果

    }

    static List<Node> toList(Tree<Node> tree) {
        List<Node> nodeList = new ArrayList<>();
        for (Tree<Node> childTree : tree.getChildren()) {
            nodeList.addAll(toList(childTree));
        }
        return nodeList;
    }

}