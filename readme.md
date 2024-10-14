# Game Sales


## PreRequisites

- Java 22
- SpringBoot 3.3.4
- Docker Compose v2.29.7
- curl
- python3

## Start App

```bash
make startApp
```

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