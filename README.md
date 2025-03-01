# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

#Server Design
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5xDAaTgALdvYoALIoAIye-Ezw-g7BKABMEZI+MYEhAMxJ3sx+AXEALFlRubEhAKyY9lAQAK7YMADEaMBUljAASij2SKoWckgQaI0A7r5IYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0NQZQADpoAN4ARGeUzQC2KLcrtzC3ADSfuOrD0A4bw+30+KCewCQCGBnwAvphhMsYPNWOwuJQ1ncHlBnq93p8frc-qoAVAgfjQbdwZDoRT4WxONxYCjEaI1lAuj0JlAABSdbq9SidACONTUYAAlAilqJkQtZPIlCp1GtAmAAKrnHnY3FShWKZRqVRyow6FYAMSQnBgmso+pgOjaOuAL0w+qVRuRLJlKjWaBqCAQ0pEKhN7sNKpAHLkKFtvOx+r12g96hNxhWCg4HBt531waospR4eVqhWUZQMYUNTAvh5wGrvkTbuTEeNKPTmezVZredZoZmKIZ6NWHU5gqgnVUAawQ6ZXoWiIxMCx51xHzWIMJ9ZrewgAGt0OvPnD85R5-BkOY1vEnE4rivHi7XjANwTPtvfLuD2gjyD4egOFMKpanqBpoDyGAABkIG6IYGlGcZJkvWYFyWJctl2Q4TgMdQBjQe9nReH5iVJDh4UXZkFlnJcHxxJ8YUpEjAQYwlqShBj6TROdvRDFA1gQGCrR5aDYJFMVeilPtDCLFsS34wS0G1Vcn2I-QSUBJNFVbNMzUta043tR0YEIsRi09AdUN4tYDO0fNC3lWSjTLaMJm7WsPybMzU3bM1OxgNy8y8tseILX1-IbXsfWkwcuKXESrUnadMGo5kQpo+5lJeX831uD8v0PF9j1uci0NShYpivGAbzvG4MsfLLCs3d8G3yn9GpPACgOqOpGiqFB0CgmDamYeCxgmTAKpQxZqHQ6QAFFILmvY5qOY4cNUPCrjy-d0FPSjUUZdLtu-N4is4w79qk+T7G2aseQEm7qzE8VJKisNHJVIwUG4CY4zrFqdrQTSDRLHS1j0rsIu0B02mOgaADIZG+lBxqC88rpgf1Azs-sYoutYROGxKEBnWKyum5ZMVuQnqxgKdWwAQmKvbz0msBr1ve9qaG2n6eVJn4TMTgupAxoOWzSCuRgABxJ9jVGxCJuQ5g0pHdZpaW1b7CfLaAe-Fm8eHKm4Z-V9mZSk0MeQXpZZeVQeVtlBnoknHpIcrS5Jga2wEd+2TeBlM2wWdMIfCntoaMk3mw98yQrZMPG1stGfItK1sxs+QYeM84ADkn2jkHY8s0K+JzO1bKk96Y8+73ff+ndAYD7SU9D32C8D9GorWR3Jd6V3zxSgmuUd4nSYuy3SsxT5tbt071juGeUAASWkU7bkpBDfsyvF18JHQEFAPc4zXd5KUXvOXlP2EYAOEqZvJtmOZq65p7lueF6fFe143sbY234FKT70Psfeip9CTnyfFfG+gtOqVG6qBbANQoDYG4PAFyhhHYjF-kraYKti7oR2PsLWOtmh63QPeCBLw75IkNkyY2ZDTZFXAU+C+eIzrJTJhPKyMBywxkdjyShKAfj+37jJaupYeHoLriI5OwddJpwToZWGDD27NzjmFAKScPpB1NKnfSuYI5OlzvnZO6jS4Z2AKo0GKd-JZhlk+SKvETSD3sS8Sc2BBgGA4ePVWVNF5fzNtQs8FkLy4Kflzfxq9AlC0AnA0WDRLDfQEsMGAAApCAVpXGGAaEAkAe4cHmC4ZTDYmx1RYWOIvXWDdvz3hQcARJUA4AQAElAH4kSgn7RcXcOpDSmktLXjlSJAzzZk3mBjAAVhkxS6SEooFFC9V28wgprEmVafhPTKB9OgG0z+0hhEMKbtYuRejIbh0zpHFRpji7x00fIKx5ljmhwsVnbErD7neTMdZAxdzZG6LmhwcY2d6qGEGDAReMBhjjF8GC3ZWcNmNOadAURVEyZrE7O4zxYgLa+OXB01mytwk3H-MLOJPUGhVHqReCssBgDYBQYQfooKFbjTZkU2aC0lorROMYA2KL8bLluBwZGEwRk+OuWFIVP0UA8klSjFAr0nFiMLp9EA3A8AKDpcgEAjLFKHIebopGUqskaF+emQ1cqy5QECtosG5qJiKIrm9WhS5pDCqdmoDxaAvHeOHGykc1wOkokftVe8xLAJAA

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

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
