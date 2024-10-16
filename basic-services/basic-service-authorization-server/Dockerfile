FROM registry.cn-guangzhou.aliyuncs.com/vains/eclipse-temurin:21-jre AS builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM registry.cn-guangzhou.aliyuncs.com/vains/eclipse-temurin:21-jre
WORKDIR application
COPY --from=builder application/extracted/dependencies/ ./
COPY --from=builder application/extracted/spring-boot-loader/ ./
COPY --from=builder application/extracted/snapshot-dependencies/ ./
COPY --from=builder application/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]