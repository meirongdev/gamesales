# Game Sales


## PreRequisites

- Java 22
- SpringBoot 3.3.4
- Docker Compose v2.29.7
- curl

## Start App

```bash
make startApp
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

## Other Command

```bash
make help
```