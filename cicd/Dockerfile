FROM openjdk:8-jdk
ARG  JAR_FILE
ENV  jar=$JAR_FILE

ARG  JAR_PORD
ENV  PORD=$JAR_PORD

COPY $jar app.jar
EXPOSE $PORD
ENTRYPOINT ["java","-jar","app.jar"] 
