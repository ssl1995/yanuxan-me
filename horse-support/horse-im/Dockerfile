FROM 857676355/skjava:8
ARG JAR_FILE
ENV jar=$JAR_FILE

ARG JAR_PORD
ENV PORD=$JAR_PORD

ARG SKNAME
ENV SKNAME=$SKNAME

ARG SKIP
ENV SKIP=$SKIP

ARG BRANCH
ENV BRANCH=$BRANCH

RUN mkdir -p /data/weblog \
    && sed -i "s@http://ftp.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
    && sed -i "s@http://security.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
    && sed -i "s@http://deb.debian.org@https://repo.huaweicloud.com@g" /etc/apt/sources.list \
    && apt-get -o Acquire::Check-Valid-Until=false update -y \
    && apt-get install apt-transport-https ca-certificates -y \
    && apt-get install vim telnet less xfonts-utils iproute2 iputils-ping -y

ENV TZ Asia/Shanghai
COPY $jar app.jar
EXPOSE $PORD
CMD java -javaagent:/usr/local/agent/skywalking-agent.jar -Dskywalking.agent.service_name=$SKNAME -Dskywalking.collector.backend_service=$SKIP -jar app.jar --spring.profiles.active=$BRANCH
