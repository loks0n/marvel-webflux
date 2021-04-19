# Marvel API

### Tech stack

- Java 11
- Spring Boot 2.4
- Spring WebFlux
- Spring Cloud GCP
- Lombok
- Guava
- JUnit 5 + Mockito 3

### Building

1. Configure Java 11 environment
2. Export google service account key location e.g. `export GOOGLE_APPLICATION_CREDENTIALS=/Users/Luke/key.json`
3. Build `$ ./gradlew build`
4. Test `$ ./gradlew test`
5. Run `$ ./gradlew bootRun`
