# aggregation-service

Service provide single endpoint **'/users'** to extract data from multi tenant. Tenants properties declarate in **'/aggregation-service/application.yml'** file.

## How to run
Download project, move to ../aggregation-service folder and run command in terminal **'docker compose up -d'**.

Technologies:
  - Java 21
  - Maven
  - Spring Boot 3+ (Web, Jdbc, Actuator, Validation, Test)
  - OpenApi Generator
  - Springdoc
  - lombok
  - Mapstruct
  - Flyway
  - JUnit
  - Testcontainers
  - Docker
  - docker compose

Service coated by WebMvcTest and integration end-to-end tests with testcontainers. End-to-end tests running by 'test' profile, properties declared in **'/aggregation-service/application-test.yml'**.

Swagger path **'http://{host}:{port}/swagger-ui/index.html'**. Swagger described by OpenApi specification in **'/aggregation-service/openapi/aggregation-api.yaml'** file.

![image](https://github.com/user-attachments/assets/8ab9241a-a335-42e9-b340-73400e6f4d7f)


