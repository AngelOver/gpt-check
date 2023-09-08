package com.example.chatcheck.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Node;
import cn.hutool.core.lang.tree.Tree;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sql3Util {




    public static List result = new ArrayList();

    public static void main(String[] args) {
        String fileContent = FileUtil.readUtf8String("D:\\soft\\Downloads\\wytree.json");
        JSONObject jsonObject = JSONObject.parseObject(fileContent);
        String soutTpye = "sql";
        JSONArray children = jsonObject.getJSONArray("response");
        int i = 0;
        for (Object child : children) {
            JSONObject jsonObject2 = JSONObject.parseObject(JSON.toJSONString(child));
            //System.out.println("1级："+jsonObject2.getString("name"));
            soutItem(jsonObject2,soutTpye);
            JSONArray children2 = jsonObject2.getJSONArray("children");
            i++;
            for (Object child3 : children2) {

                JSONObject jsonObject3 = JSONObject.parseObject(JSON.toJSONString(child3));
                JSONArray children3 = jsonObject3.getJSONArray("children");
                // System.out.println("==2级："+jsonObject3.getString("name"));
                soutItem(jsonObject3,soutTpye);
               // System.out.println("INSERT INTO `nice_littlewheat`.`ai_curses`( `title`, `en_title`, `cate_id`) VALUES ( '"+jsonObject3.getString("category_name") +"', '"+jsonObject3.getString("category_code") +"', "+i+");");

                if(children3==null){
                    continue;
                }
                for (Object child4 : children3) {

                    JSONObject jsonObject4 = JSONObject.parseObject(JSON.toJSONString(child4));
                    JSONArray children4 = jsonObject4.getJSONArray("");
                    //      System.out.println("=====3级："+jsonObject4.getString("name"));
                    soutItem(jsonObject4,soutTpye);
            //        System.out.println(jsonObject4.getString("prompt_keyword")+i);
                    //System.out.println("INSERT INTO `nice_littlewheat`.`ai_curses`( `title`, `en_title`, `cate_id`) VALUES ( '"+jsonObject4.getString("prompt_keyword") +"', '"+jsonObject4.getString("prompt_code") +"', "+i+");");
                }
            }
        }
        System.out.println(JSONArray.toJSONString(result));


        // 使用TreeUtil创建Tree对象

        // 将Tree对象转换为列表

        // 打印结果

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