FROM bigtruedata/sbt:0.13.15-2.12.2
MAINTAINER Juan Pablo Royo <juanpablo.royo@gmail.com>


WORKDIR /opt/twitter-api

ADD . /opt/twitter-api/
RUN sbt pack

EXPOSE 8080

CMD ["/opt/twitter-api/target/pack/bin/twitter-api"]
