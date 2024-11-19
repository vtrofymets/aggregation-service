FROM maven:3.9.9 as maven
WORKDIR /app
COPY .. /app/.
RUN mvn clean package -Dmaven.test.skip=true


FROM openjdk:21-slim as builder
COPY --from=maven /app/target/*.jar aggregation.jar
ARG JAR_FILE=aggregation.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract


FROM openjdk:21-slim
VOLUME /tmp
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]