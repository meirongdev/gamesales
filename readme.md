# Game Sales


## PreRequisites

- Java 22
- SpringBoot 3.3.4
- Docker Compose v2.29.7


## Start App

```bash
./gradlew bootRun
```


## Endpoint test

### `import`

```bash
curl -X POST http://localhost:8080/import \
	-F "file=@/home/mr/projects/spring/gamesales/test.csv" \
	-H "Content-Type: multipart/form-data"
```