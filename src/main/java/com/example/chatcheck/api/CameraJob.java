//package com.example.chatcheck.api;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//@Slf4j
//public class CameraJob {
//
//    @Scheduled(cron = "0 0 * * * ? ")
//    public void DeviceCountAllUpdate() throws JsonProcessingException {
//        log.info(DateUtil.now()+"视频系统告警监控");
//        SpringUtil.getBean(CameraController.class)
//                .monitorCameraAlarm(60);
//
//    }
//
//
//
//
//}
