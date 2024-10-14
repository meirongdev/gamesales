# Game Sales


## PreRequisites

- Java 22
- SpringBoot 3.3.4
- Docker Compose v2.29.7
- curl
- python3

## Architecture

![architecture](./docs/images/arch.svg)

- kafka: store import task, make the import async; support cdc
- file system: store csv files
- debezium: CDC(change data capture), monitor `game_sales` table, generate the aggregated data for `daily_sales` table.

## Start App

```bash
make startApp
```

![startApp](./docs/images/startApp.gif)

## Config mysql cdc

Use `debezium` to detect the change of the table `game_sales`.

```bash
make gamesalesConnector
```

## Endpoint test

import csv
```bash
make testImport
```

`/GetGameSales` endpoint
```bash
make testGetGameSales
make testGetGameSalesByDuration
make testGetGameSalesBySalePriceComparison
```

`/getTotalSales` endpoint
```bash
make testGetTotalSalesByDuration
make testGetTotalSalesByDurationAndGameNo
```

generate 100M game sales record

```bash
make genData
```

```bash
make testImport1M
```

## Other Command

```bash
make help
```

## Screenshots

### Related makefile script
```makefile
.PHONY: testImport
# Test the /import endpoint
testImport:
	@curl -X -w %{time_total} POST http://localhost:8080/import \
		-F "file=@test.csv" \
		-H "Content-Type: multipart/form-data"

.PHONY: testGetGameSales
# Test the /getGameSales endpoint
testGetGameSales:
	@curl -w %{time_total} http://localhost:8080/getGameSales

.PHONY: testGetGameSalesByDuration
# Test the /getGameSales endpoint with duration, eg: curl -v GET http://localhost:8080/getGameSales\?fromDate\=2021-10-09T00:00:00Z\&toDate\=2024-10-10T23:59:59Z
testGetGameSalesByDuration:
	@curl -w %{time_total} http://localhost:8080/getGameSales\?fromDate\=2024-04-09T00:00:00Z\&toDate\=2024-04-10T23:59:59Z

.PHONY: testGetGameSalesBySalePriceComparison
# Test the /getGameSales endpoint with sale price, eg: curl -v GET http://localhost:8080/getGameSales\?salePrice\=20\&priceComparison\=less
testGetGameSalesBySalePriceComparison:
	@curl -w %{time_total} http://localhost:8080/getGameSales\?salePrice\=20\&priceComparison\=less


.PHONY: testGetTotalSalesByDuration
# Test the /getTotalSales endpoint with duration, eg: curl -v GET http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10
testGetTotalSalesByDuration:
	@curl  -w %{time_total} http://localhost:8080/getTotalSales\?fromDate\=2024-04-09\&toDate\=2024-04-10

.PHONY: testGetTotalSalesByDurationAndGameNo
# Test the /getTotalSales endpoint with duration and gameNo, eg: curl -v GET http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10\&gameNo\=50
testGetTotalSalesByDurationAndGameNo:
	@curl -w %{time_total} http://localhost:8080/getTotalSales\?fromDate\=2024-04-09\&toDate\=2024-04-10\&gameNo\=50
```

### `/import`

![import](./docs/images/import.gif)

### `import/status/{trace_no}`

![import](./docs/images/import_status.gif)

### `/getGameSales`

![getGameSales](./docs/images/getGameSales.gif)

![getGameSalesByDuration](./docs/images/getGameSalesByDuration.gif)

![getGameSalesBySalePriceComparison](./docs/images/getGameSalesBySalePriceComparison.gif)


### `/getTotalSales`

![getTotalSalesByDuration](./docs/images/getTotalSalesByDuration.gif)

![getTotalSalesByDurationAndGameNo](./docs/images/getTotalSalesByDurationAndGameNo.gif)


### `Import 1000,000 game sales records`

![import1m](./docs/images/import_1m.gif)

![import 1m status](./docs/images/import_status_1m.gif)

> "createdAt":"2024-10-14T14:47:47.555949Z","updatedAt":"2024-10-14T14:47:53.316733Z"

The time for importing(diff between **updatedAt** and **createdAt**) is less then 5s.

