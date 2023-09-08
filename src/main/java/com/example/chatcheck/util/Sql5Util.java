package com.example.chatcheck.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Node;
import cn.hutool.core.lang.tree.Tree;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sql5Util {




    public static List result = new ArrayList();

    public static void main(String[] args) {
        String fileContent = FileUtil.readUtf8String("D:\\soft\\Downloads\\wy.json");

        String indexStr="3,4,5,6,7,8,9,10,11,12,13,15,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,50,51,52,53,54,55,56,57,58,59,511,512,513,514,61,62,63,64,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,132,134,135,136,137,138,139,140,143,144,146,147,148,149,150,151,152,153,156,157,158,159,160,161,162,458,459,470,155,165,166,167,168,169,171,172,173,174,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,460,461,462,463,464,467,468,207,208,209,210,211,212,213,214,215,216,217,218,219,220,222,223,224,225,226,227,228,229,230,231,509,510,234,236,238,240,242,243,245,247,249,250,252,254,256,258,260,262,264,266,268,270,273,275,278,280,281,282,283,284,285,286,287,288,289,290,292,293,294,295,296,298,299,300,302,303,304,305,306,307,309,310,311,313,314,315,316,317,318,319,321,322,323,324,325,326,327,328,457,330,331,332,333,335,336,338,339,340,341,343,344,346,347,348,349,465,466,351,353,355,357,456,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,408,409,410,411,412,413,414,469,416,417,418,419,420,421,422,423,424,425,426,428,429,430,431,432,433,434,435,436,437,438,439,440,442,445,446,447,449,451,454,504,505,506,507,508";
        JSONArray jsonObject = JSONObject.parseArray(fileContent);
        String[] index = indexStr.split(",");


        System.out.println(jsonObject.size());
        String soutTpye = "sql";
        JSONArray children = jsonObject;
        int i = 0;
        for (Object child : children) {
            JSONObject jsonObject2 = JSONObject.parseObject(JSON.toJSONString(child));
            //System.out.println("1级："+jsonObject2.getString("name"));
           // soutItem(jsonObject2,soutTpye);

            String rootId = index[i];
            jsonObject.get(i);
            JSONObject children2 = jsonObject2.getJSONObject("response");
            JSONArray items = children2.getJSONArray("checkItems");
            for (int j = 0; j <items.size() ; j++) {
                JSONObject jsonObject3 = items.getJSONObject(j);
                jsonObject3.put("tree_id",rootId);
                //System.out.println(jsonObject3);
                result.add(jsonObject3);
            }
            i++;

        }
        System.out.println(JSONArray.toJSONString(result));


        // 使用TreeUtil创建Tree对象

        // 将Tree对象转换为列表

        // 打印结果

    }

    public static void soutItem(JSONObject obj, String type){
//        if("sql".equals(type)){
//            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(obj));
//            JSONArray children = obj.getJSONArray("children");
//            if(children!=null) {
//                jsonObject.put("children", children.size());
//            }else {
//                jsonObject.put("children",0);
//            }
//            //System.out.println(jsonObject);
//            result.add(jsonObject);
//        }else {
//            System.out.println(obj.get("name"));
//        }

    }

    static List<Node> toList(Tree<Node> tree) {
        List<Node> nodeList = new ArrayList<>();
        for (Tree<Node> childTree : tree.getChildren()) {
            nodeList.addAll(toList(childTree));
        }
        return nodeList;
    }

}