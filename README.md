# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Server
Server design [diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco4LpOzz6K8SwgX0ALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhyMDcryBqCsKMCvmIrrSkml4IeULGOs6BIESy5Set6QYBkGIZseakY0dGMCxsG2i8YmyawSW6E8tmuaYP+ILwSUVwDGRoy7F806zs246ttB7a6YU2Q9jA-aDr0hkjsZlnVlOQbmfOHltmYnCrt4fiBF4KDoHuB6+Mwx7pJkmD2ReRTUNe0gAKK7ml9Rpc0LQPqoT7dGZjboDZbI6eUxVztpqm-slTryjAyH2NFaFRb6mEYjhcp4fxlGETA5JgPJYl1iVaAUUybrUeUdExuJ2hCmE8k+aG-XsXp4JyQt8hKb1MgCaSRgoNwmQjdOEmHRGhSWjIJ0UoY8nxj1nYVZFLUdWoWk6Z2P5XN+V62V2ORgH2A5DgcAUrp4wUbpCtq7tCMAAOKjqysWngl57MHp17I1luX2KORXeeNZUqUCJZVaVP2bY1yCxKjoyqKNM7jd1CEaldRHDaJ1MTWtU3sTdNFzdtcaLUx-OC2aEZ04h4sKbtuFc+tR1DUzaiwpNsszbRPK2prDqSXLnENQrRuKSrHFnJTaaI4zaOaQgea1b9gNXFMRPM+MMDdBU-TeygACS0gmU8J6ZAaFafE8OgIKADbR8Bsde6OAByo7-DAjQAylQOJaDjng70ac++U-uB6Oofh1Mkf6kZ9yeX08eJ8nxmp30QeZ6M2e51DQXroE2A+FA2DcPAuqZCjo4pHFZ4g2yf2VLUDSE8TwSk3OQ7d6OeclBTqaVVv6A7xne81XbS89UJU8oJrsJwHfnXYXtqtC+rFIP7vow61R0mzQNjPUYjEwiax7qxK67suIwEVMqN++E1bulgSgZI39z6-xlv-EW5RZKW2Vi9G2b0n5ekyJrZ2rsr422XlXUYNc-LWR-J2QuYNnK0JDmHBhS5AowyHgESwJ1kLJBgAAKQgDyYBhgAitxAA2LGi9cZpmqJSO8LQg4kzGtvXo49gACKgHACAyEoCzCDqHfeHZD7AmPpo0+2iE56IMUYkx1dpAwTtgUXC5QABW4i0APzERpFAaIuoIL6h-ZBGtRywlMdIP+00AH615Pg4AS1JGhywfE+W5RkmhIOkgnm50T4CxNnrMWT1JZhGliU6hN9FbPU5og8J5QDBUkbrCHRDjDHQFmOU+QcThbsnKBQZgOTraWJLAEvxo4KGX1TNA-SpZzEF2xqwiGA9eEhQCF4XRXYvSwGANgcehB4iJDnpjQu1987lAqOlTK2VcrGHJn+Wq5QOD3XirTM2W0QDcDwI-X5WYglYSxLkk2rz3koBGv0qSOC7qnUejtYAoLubHXhSRaF11BlwoegxK2hCwmy3BfCh+GK9bSAhaMwh4y0yID2TM2mij-rk1OCw4ubDIbLkwEAA)

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
