FROM harbor.avalaunch.aval/infra-resources/maven-3.9.5:21.0.2 as maven
WORKDIR /app
COPY .. /app/.
RUN mvn clean package -Dmaven.test.skip=true


FROM harbor.avalaunch.aval/docker-hub-proxy/openjdk:21-slim as builder
COPY --from=maven /app/target/*.jar aggregation.jar
ARG JAR_FILE=aggregation.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract


FROM harbor.avalaunch.aval/docker-hub-proxy/openjdk:21-slim
VOLUME /tmp
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]