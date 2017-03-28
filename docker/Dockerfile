FROM openjdk:8

MAINTAINER Jim Callahan <james.michael.callahan@gmail.com>

RUN mkdir -p /opt/maverick/jars/
ADD jars/* /opt/maverick/jars/
COPY maverick.jar /opt/maverick/

ADD entrypoint.sh /
RUN chmod -c 755 /entrypoint.sh 

ENTRYPOINT ["/entrypoint.sh"]
