FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/order-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./order-service-0.0.1-SNAPSHOT.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./order-service-0.0.1-SNAPSHOT.jar"]