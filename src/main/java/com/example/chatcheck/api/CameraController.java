//package com.example.chatcheck.api;
//
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.lang.func.Func1;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.http.HttpUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.nio.charset.Charset;
//import java.util.*;
//
///**
// * 设备子系统配置(DeviceSystemConfig)表控制层
// *
// * @author zhangliang
// * @since 2022-12-02 17:21:03
// */
//@RestController
//@Slf4j
//    @RequestMapping("/deviceSystemConfig")
//public class CameraController {
//
//
//    static String mockIotHost =  "10.82.27.233:39900";
//
//    static int minSpace = 15;
//
//
//     static boolean sqlFlag = false;
//
//
//    static String alarmDetail = null;
//    /**
//     * 每日定时造设备事件数据
//     *
//     * @return 所有数据
//     */
//
//
//
//    /**
//     * 监控摄像头告警
//     *
//     * @return 所有数据
//     */
//    @GetMapping("/monitorCameraAlarm")
//    public List<String> monitorCameraAlarm(Integer min) {
//        minSpace = min;
//        List<String> strings = monitorCameraAlarm(MockData::toCameraAlarm);
//        for (String msg : strings) {
//            System.out.println(msg);
//        }
//        return strings;
//    }
//
//    public static void main(String[] args) {
//        List<String> strings = new CameraController().monitorCameraAlarm(60*24*3);
//        for (String msg : strings) {
//            System.out.println(msg);
//        }
//    }
//
//    private List<String> monitorCameraAlarm(  Func1<MockData, JSONObject> mockDateFunc) {
//
//        Map<String, String> sortMap = new HashMap<>();
//
//        List<String> msg = new ArrayList<>();
//        String deviceName = "运维平台OMS";
//        String productKey = "3d9f630c7c6d";
//        String identifier = "CameraAlarmEvent";
//        String startTime = DateUtil.format(DateUtil.offsetMinute(DateUtil.date(), -(minSpace)), "yyyy-MM-dd HH:mm:ss");
//        String endTime = DateUtil.now();
//        log.info("监控摄像机告警,开始时间："+startTime+"结束时间："+endTime);
//
//        String listUrl = "http://10.82.33.181:8080/bods.svc/GetAlarmEvent?token=%s&STARTTIME='%s'&ENDTIME='%s'&FROM='0'&TO='100'";
//        String detailUrl = "http://10.82.33.181:8080/bods.svc/GetAlarmDetails?token=%s&EQUIPMENTRECID='%s'";
//
//        String tokenS = HttpUtil.get("http://10.82.33.181:8080/rest/auth/login?user=admin&passwd=bWFuYWdl&_=1700816897711");
//        String token = JSONObject.parseObject(tokenS).getString("token");
//        listUrl = String.format(listUrl, token, startTime, endTime);
//        listUrl = HttpUtil.encodeParams(listUrl, Charset.defaultCharset());
//        String listS = HttpUtil.get(listUrl);
//        JSONArray results = JSONObject.parseObject(listS).getJSONObject("d").getJSONArray("results");
//        if (results.size() > 0) {
//            String dataListS = JSONObject.parseObject(JSONObject.toJSONString(results.get(0))).getString("EVENTDATA");
//            JSONArray dataList = JSONArray.parseArray(dataListS);
//            log.info("监控摄像机告警,记录总数："+dataList.size());
//            for (Object o : dataList) {
//                JSONObject detailObj = JSONObject.parseObject(JSONObject.toJSONString(o));
//                String equipmentRecId = detailObj.getString("equipmentRecId");
//                String detailS = HttpUtil.get(HttpUtil.encodeParams(String.format(detailUrl, token, equipmentRecId), Charset.defaultCharset()));
//
//                //System.out.println(detailS);
//                // System.out.println(JSONObject.toJSONString(o));
//                JSONObject pJ = null;
//                String sortKey = null;
//                try {
//
//                    Object itemObj = JSONObject.parseObject(detailS).getJSONObject("d").getJSONArray("results").get(0);
//                    String itemStr = JSONObject.parseObject(JSONObject.toJSONString(itemObj)).getString("ALARMEVENTDETAIL");
//                    String itemStr2 = JSONArray.parseArray(itemStr).get(0).toString();
//                    JSONObject item = JSONObject.parseObject(itemStr2);
//                    //detailObj.putAll(item);
//                    detailObj.put("cameraId", item.getString("cameraId"));
//                    detailObj.put("imageUrl", item.getString("imageUrl"));
//                    alarmDetail = detailObj.toString();
//                    pJ = mockDateFunc.call(new MockData());
//                    sortKey = pJ.getString("sortKey");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                JSONObject param = mockDataBuild(identifier, productKey, deviceName, pJ);
//                String jsonKey = param.toString();
//                sortMap.put(sortKey, jsonKey);
//            }
//        }
//        MapUtil.sort(sortMap).entrySet().stream().forEach(entry -> {
//            JSONObject param = JSONObject.parseObject(entry.getValue());
//            msg.add(param.toString());
//            String res = param.getString("deviceName") + HttpUtil.post("http://" + mockIotHost + "/device/things/eventReport", entry.getValue());
//            msg.add(res);
//        });
//        log.info("监控摄像机告警,结束========================");
//        return msg;
//    }
//
//    class MockData{
//        JSONObject ps;
//        String productKey = null; //门禁达实
//        String identifier = null;
//
//        public JSONObject toCameraAlarm(){
//            //{"equipmentip":"三楼客梯人脸考勤","lasttime":"2023-11-24 16:55:27","alarmtime":"2023-11-24 16:55:27","alarmcontent":"清晰度","equipmentstatus":"warning","eventstatus":"1","recid":"04B676EF4ECA4CBA81884B63FDAB7A8D","equipmentRecId":"10000107001320000087"}
//            //摄像机ID CameraId
//            //摄像机名称 CameraName
//            //报警内容 AlarmContent
//            //报警时间 AlarmTime
//            //事件状态 EventStatus
//            //事件记录id EventId
//            //摄像机状态 CameraStatus
//            //摄像机图片 CameraImg
//            JSONObject detail = JSONObject.parseObject(alarmDetail);
//            productKey="3d9f630c7c6d";
//            identifier="CameraAlarmEvent";
//            ps = new JSONObject();
//            ps.put("CameraId", detail.getString("cameraId"));
//            ps.put("CameraName", detail.getString("equipmentip"));
//            ps.put("AlarmContent", detail.getString("alarmcontent"));
//            ps.put("AlarmTime", detail.getString("alarmtime"));
//            ps.put("CameraImg", detail.getString("imageUrl"));
//            ps.put("EventStatus", detail.getString("eventstatus"));
//            ps.put("EventId", detail.getString("recid"));
//            ps.put("CameraStatus", detail.getString("equipmentstatus"));
//
//            ps.put("sortKey",ps.getString("AlarmTime"));
//            return ps;
//        }
//
//    }
//
//
//    /**
//     * 构建事件实体
//     * @param identifier
//     * @param productKey
//     * @param deviceId
//     * @param pj
//     * @return
//     */
//    private static JSONObject mockDataBuild(String identifier,String productKey,String deviceId,JSONObject pj) {
//        JSONObject all = new JSONObject();
//        JSONObject event = new JSONObject();
//        event.put("identifier",identifier);
//        event.put("data",pj);
//        all.put("identifier","default");
//        all.put("action","eventReport");
//        all.put("deviceName",deviceId);
//        all.put("productKey",productKey);
//        all.put("event",event);
//
//        pj.remove("sortKey");
//        return all;
//    }
//
//
//
//
//
//
//
//}
//
