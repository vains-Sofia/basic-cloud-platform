FROM registry.cn-guangzhou.aliyuncs.com/vains/maven:3.9-ibm-semeru-21-jammy AS build
WORKDIR /app
RUN cd ../../
RUN ll
COPY . .
RUN mvn clean install -s settings.xml -DskipTests
RUN cd basic-services/basic-service-authorization-server
RUN mvn package -s settings.xml -DskipTests
# 如果使用国内网络环境打包可以使用下边的命令，正在使用的是Github Action打包时使用的，国外环境无影响
# RUN mvn clean package -s settings.xml -DskipTests

FROM registry.cn-guangzhou.aliyuncs.com/vains/eclipse-temurin:21-jre AS builder
WORKDIR application
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM registry.cn-guangzhou.aliyuncs.com/vains/eclipse-temurin:21-jre
WORKDIR application
COPY --from=builder application/extracted/dependencies/ ./
COPY --from=builder application/extracted/spring-boot-loader/ ./
COPY --from=builder application/extracted/snapshot-dependencies/ ./
COPY --from=builder application/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]