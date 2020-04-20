FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD build/libs/bowling-0.0.1-SNAPSHOT.jar bowling.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bowling.jar"]