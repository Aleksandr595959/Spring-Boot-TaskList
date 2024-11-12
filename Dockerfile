FROM openjdk:17-jdk
ARG JAR_FILE=target/webapp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} webapp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/webapp-0.0.1-SNAPSHOT.jar"]