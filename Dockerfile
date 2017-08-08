FROM bigtruedata/sbt:0.13.15-2.12.2
MAINTAINER Juan Pablo Royo <juanpablo.royo@gmail.com>


WORKDIR /opt/twitter-api

ADD . /opt/twitter-api/

EXPOSE 8080

CMD ["sbt", "run"]
