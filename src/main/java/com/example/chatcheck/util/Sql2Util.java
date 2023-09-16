package com.example.chatcheck.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Node;
import cn.hutool.core.lang.tree.Tree;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sql2Util {



    public static void main(String[] args) {
        String fileContent = FileUtil.readUtf8String("D:\\soft\\Downloads\\敏感词.txt");
        fileContent = fileContent.replaceAll("\r","");
        fileContent = fileContent.replaceAll("\n","");
        fileContent = fileContent.replaceAll(";","");
        fileContent = fileContent.replaceAll("\r","");
        fileContent = fileContent.replaceAll(" ","");

        fileContent = fileContent.replaceAll("\\)\\/\"","");
        String[] split = fileContent.split("\\|");
        ArrayList<String> strings = CollUtil.newArrayList(split);
        ArrayList<String> result = CollUtil.newArrayList(split);

        for (String string : strings) {
            if(string.length()>1){
               // System.out.println("==="+string);
                result.add(string.trim());
            }
        }
        int size = result.size();
        int batchSize = 1000;
        for (int i = 0; i < size; i += batchSize) {
            List<String> sublist = result.subList(i, Math.min(i + batchSize, size));
            String joinedString = String.join("|", sublist);
            System.out.println("$keywordsChat"+i + "=\"/("+joinedString+")/\";");
            System.out.println("if(preg_match($keywordsChat"+i+",$info,$matches))   return Response()->json(['message'=>'禁止发送违规词"+i+"','status'=>403],403);");
        }

        System.out.println(split.length);

    }

    static List<Node> toList(Tree<Node> tree) {
        List<Node> nodeList = new ArrayList<>();
        for (Tree<Node> childTree : tree.getChildren()) {
            nodeList.addAll(toList(childTree));
        }
        return nodeList;
    }

}