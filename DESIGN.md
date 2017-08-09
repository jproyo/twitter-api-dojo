# Twitter Proxy API Design Comments

## Architecture

The Architecture basically has 3 layers:

1. External or Consumer Layer
2. Delegator
3. Business Service
4. Adapter

### External or Consumer Layer

Consumer Layer which in this case is a HTTP REST API. This layer redirects requests to another layer which i called **Delegator** and handles bad request or unexpected exceptions.

This layer was implemented with [**akka-http**](http://doc.akka.io/docs/akka-http/current/scala/http/index.html) libraries, taking the advantage of Routing DSL flavors. I have chosen this library because i wanted to implement **Delegator** layer with Actors. This decision is based on the assumption that perhaps in some future version of the product we are going to need some orchestration level to handle several request in parallel and Actor model give us the perfect flexibility for this.

#### Files

- Main.scala
- Controller.scala

### Delegator Actor

In the first version of the product this layer only delegates to the Specific Business Service to collect data, but as i have pointed out above it should be beneficial for future version to have an intermediate layer to split up request and join responses.

#### Files

- DelegatorActor.scala

### Business Service

The Business Service has the [Implicit Context Pattern](http://www.lihaoyi.com/post/ImplicitDesignPatternsinScala.html) implemented to decouple interface from implementation.
This layer handles cache results and if it is not present in the cache it calls to underlying real implementation.

#### Files

- service/Service.scala

### Adapter

Although the Architecture is not 100% hexagonal, Adapter layer was thought with an Hexagonal approach in order to be able to change or plug different implementations of the real communication with Twitter API.

#### Files

- adapter/Adapter.scala


## Development Process

I have used a mixed between TDD approach and top down development. I have started working with **RouteIntegrationTest** as my entry point to the application thinking from the user's side and not from the implementation one.

After filling some gaps i started to build implementations and complete unit and integration testing.

## Time for solution completion

14 hours ETA
