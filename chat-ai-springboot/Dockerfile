# 构建阶段
FROM maven:3.8.5-jdk-8 AS build

# 配置maven阿里云镜像
RUN sed -i '/<\/mirrors>/i \
    <mirror>\n\
        <id>aliyunmaven</id>\n\
        <name>aliyun maven</name>\n\
        <url>https://maven.aliyun.com/repository/public</url>\n\
        <mirrorOf>central</mirrorOf>\n\
    </mirror>\n' /usr/share/maven/conf/settings.xml

WORKDIR /chat-ai
COPY pom.xml .
COPY src ./src

# 打包命令（跳过测试）
RUN mvn clean package -DskipTests -B -e -X

# 运行阶段
FROM openjdk:8-jre-alpine
WORKDIR /chat-ai
COPY --from=build /chat-ai/target/chat-ai-springboot.jar chat-ai-springboot.jar

# 时区设置
RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone \

# 对外暴露 8090 端口
EXPOSE 8089

#Java 配置
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dfile.encoding=UTF-8"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar chat-ai-springboot.jar"]