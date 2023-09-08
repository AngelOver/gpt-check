package com.example.chatcheck.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.example.chatcheck.util.GPTPlusUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 设备列表对外接口，对其他模块
 */
@RestController
@RequestMapping("/plus")
public class ChatPlusController {

    /**
     * 设备资源接口-对外接口-分页查询
     *
     * @return 所有数据
     */
    @GetMapping("/chat")
    public void chat(String msg, HttpServletResponse response) throws IOException {
        TimeInterval timer = DateUtil.timer();
        try {
            timer.start();
            msg = msg.trim();
            String replyStr = GPTPlusUtil.sendMsg(msg);
            if (replyStr == null) {
                response.getWriter()
                        .write("调用接口失败，plus接口异常");
                return;
            }
            // 设置响应头部信息，包括"Content-Type: text/event-stream"和"Transfer-Encoding: chunked"
            response.setContentType("text/event-stream");
            response.setHeader("Transfer-Encoding", "chunked");
            // 将"sendMsg"方法返回的"text/event-stream"类型的字符串作为响应体输出
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] responseBytes = replyStr.getBytes("UTF-8");
            String responseHexLength = Integer.toHexString(responseBytes.length) + "\r\n";
            String responseHexEnd = "\r\n";
            outputStream.write(responseHexLength.getBytes("UTF-8"));
            outputStream.write(responseBytes);
            outputStream.write(responseHexEnd.getBytes("UTF-8"));
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter()
                    .write("处理失败" + e.getMessage());
        } finally {
            System.out.println("调用完成：耗时(s)：" + timer.intervalMs() * 1.0 / 1000);
        }
    }

    @GetMapping("/chat2")
    public void chat2(String msg, HttpServletResponse response) {
        try {
            response.setContentType("text/event-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            final PrintWriter writer = response.getWriter();

            GPTPlusUtil.sendMsg(msg, new GPTPlusUtil.ResponseHandler() {
                @Override
                public void handleLine(String line) {
                    writer.write(line + "\n");
                    writer.flush();
                }

                @Override
                public void handleError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void handleComplete() {
                    writer.close();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/chat3")
    public void chat3(String msg, HttpServletResponse response) {
        try {
            response.setContentType("text/event-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            final PrintWriter writer = response.getWriter();

            GPTPlusUtil.sendMsg3(msg, new GPTPlusUtil.ResponseHandler() {
                @Override
                public void handleLine(String line) {
                    writer.write("data: " + line + "\n\n");
                    writer.flush();
                }

                @Override
                public void handleError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void handleComplete() {
                    writer.close();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @PostMapping("/chat4")
    public void chat4(String msg, HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        GPTPlusUtil.sendMsg4(msg, response);
    }

}
