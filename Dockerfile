FROM 857676355/skjava:8
COPY --from=hengyunabc/arthas:latest /opt/arthas /arthas
ARG JAR_FILE
ENV jar=$JAR_FILE

ARG JAR_PORD
ENV PORD=$JAR_PORD

ARG SKNAME
ENV SKNAME=$SKNAME

ARG SKIP
ENV SKIP=$SKIP

ARG NACOS_URL 
ENV NACOS_URL=$NACOS_URL 

ARG NACOS
ENV NACOS=$NACOS

ARG NACOS_PS 
ENV NACOS_PS=$NACOS_PS 

ARG BRANCH
ENV BRANCH=$BRANCH

#RUN mkdir -p /data/weblog \
#    && sed -i "s@http://ftp.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
#    && sed -i "s@http://security.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
#    && sed -i "s@http://deb.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
#    && apt-get -o Acquire::Check-Valid-Until=false update -y \
#    && apt-get install apt-transport-https ca-certificates -y \
#    && apt-get install vim telnet less xfonts-utils iproute2 iputils-ping -y

ENV TZ Asia/Shanghai
COPY $jar app.jar
EXPOSE $PORD
CMD java -javaagent:/usr/local/agent/skywalking-agent.jar -Dskywalking.agent.service_name=$SKNAME -Dspring.cloud.nacos.discovery.server-addr=$NACOS_URL -Dspring.cloud.nacos.discovery.username=$NACOS -Dspring.cloud.nacos.discovery.password=$NACOS_PS -Dskywalking.collector.backend_service=$SKIP -jar app.jar --spring.profiles.active=$BRANCH
