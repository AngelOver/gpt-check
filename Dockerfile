FROM openjdk:11-jre-slim

# 创建一个新目录来存储jdk文件
WORKDIR /home/infinova/device-management
ENV TZ Asia/Shanghai
ADD ./target/chat-check-0.0.1-SNAPSHOT.jar /home/infinova/
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-Xms512M", "-Xmx512M", "-jar",  "/home/infinova/chat-check-0.0.1-SNAPSHOT.jar"]