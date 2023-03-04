FROM openjdk:8-jdk
ARG  JAR_FILE
#COPY ${JAR_FILE} app.jar
COPY $DIR/$JAVA app.jar
EXPOSE  $PORD 
ENTRYPOINT ["java","-jar","app.jar"] 