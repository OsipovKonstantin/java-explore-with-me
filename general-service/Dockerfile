FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar general-service.jar
ENTRYPOINT ["java","-jar","/general-service.jar"]