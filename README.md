# Twitter Proxy API

## Requirements

We need a small REST API with just one endpoint that, given a Twitter username, returns the user’s last 10 tweets, in JSON format.

- Feel free to use any frameworks or libraries you may need, even to communicate with Twitter.
- If you want to use a framework, take into account that we use Akka and Play in Scala/Java.
- Your application should not save anything to MySQL or to any other relational DB.

## Bonus

- Improve your application’s speed by using a cache.

## We like clean code

- Code tested with unit-testing and integration testing.
- Usage of architectures and paradigms like Hexagonal Architecture and DDD.
- Usage of design patterns.
- Show off!

## Server listening

Server is listening at http://localhost:8080/twitter/api

To Test this out you can try it with curl or other HTTP client

```shell
curl -v http://localhost:8080/twitter/api/ping
```

## Run Solution

### Without Docker

#### Prerequisites

In order to run this solution you are going to need the following distributions installed.

- SBT 0.13.15+
- Scala 2.12.2+

#### Run tests

```shell
bash.$ sbt it:test test
```

#### Run Coverage Report

```shell
bash.$ sbt clean coverage it:test test coverageReport
```

#### Startup Application with SBT

```shell
bash.$ sbt run
```

#### Package Executable JAR

```shell
bash.$ sbt pack
```

##### Execute JAR

```shell
bash.$ ./target/pack/bin/twitter-api
```

### With Docker

#### Prerequisites

Docker 17.06+

#### Run Test

```shell
bash.$ docker build -t twitter-api .
bash.$ docker run twitter-api sbt it:test test
```

#### Run Solution

```shell
bash.$ docker build -t twitter-api .
bash.$ docker run -p8080:8080 twitter-api
```

#### Run Coverage Report

```shell
bash.$ docker build -t twitter-api .
bash.$ docker run -p8080:8080 twitter-api sbt clean coverage it:test test coverageReport
```
