//package com.example.chatcheck.controller.api.dto;
//
//import cn.hutool.core.io.BufferUtil;
//import cn.hutool.core.io.IORuntimeException;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.socket.nio.NioServer;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Entities;
//import org.jsoup.safety.Cleaner;
//import org.jsoup.safety.Whitelist;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//
//@Component
//@SpringBootConfiguration
//public class WSServer {
//
//
//    @Bean
//    void NioServer(){
//        NioServer server = new NioServer(9082);
//        server.setChannelHandler((sc)->{
//            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//            try{
//                System.out.println("收到消息");
//                //从channel读数据到缓冲区
//                int readBytes = sc.read(readBuffer);
//                if (readBytes > 0) {
//                    //Flips this buffer.  The limit is set to the current position and then
//                    // the position is set to zero，就是表示要从起始位置开始读取数据
//                    readBuffer.flip();
//                    //eturns the number of elements between the current position and the  limit.
//                    // 要读取的字节长度
//                    byte[] bytes = new byte[readBuffer.remaining()];
//                    //将缓冲区的数据读到bytes数组
//                    readBuffer.get(bytes);
//                    String body = StrUtil.utf8Str(bytes);
//                    Console.log("[{}]: {}", sc.getRemoteAddress(), body);
//                    doWrite(sc, body);
//                } else if (readBytes < 0) {
//                    IoUtil.close(sc);
//                }
//            } catch (IOException e){
//                throw new IORuntimeException(e);
//            }
//        });
//        server.listen();
//    }
//
//    public static void doWrite(SocketChannel channel, String response) throws IOException {
//        response = "收到消息：" + response;
//        //将缓冲数据写入渠道，返回给客户端
//        channel.write(BufferUtil.createUtf8("okokok"));
//    }
//
//    public static String convertHtml(String html, String charset) throws IOException {
//        Document doc = Jsoup.parse(html, charset);
//
//        return parseDocument(doc);
//    }
//
//    private static int indentation = -1;
//    private static String parseDocument(Document dirtyDoc) {
//        indentation = -1;
//
//        String title = dirtyDoc.title();
//
//        Whitelist whitelist = Whitelist.relaxed();
//        Cleaner cleaner = new Cleaner(whitelist);
//
//        Document doc = cleaner.clean(dirtyDoc);
//        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
//
//        if (!title.trim().equals("")) {
//            return "# " + title + "\n\n" + getTextContent(doc);
//        } else {
//            return getTextContent(doc);
//        }
//    }
//
//
//}
