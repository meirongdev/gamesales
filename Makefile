.PHONY: startApp
# Start spring boot app
startApp: startInfra
	@echo "Running the program..."
	@./gradlew bootRun

.PHONY: startInfra
# Start the infras dependencies, eg: mysql
startInfra:
	@echo "Starting the infras..."
	@cd docker && docker compose up -d

.PHONY: stopInfra
# Stop the infras dependencies, eg: mysql
stopInfra:
	@echo "Stopping the infras..."
	@cd docker && docker compose down

.PHONY: stopApp
# Stop spring boot app
stopApp:stopInfra
	@echo "Stopping the program..."
	@./gradlew --stop

.PHONY: testImport
# Test the /import endpoint
testImport:
	@curl -X POST http://localhost:8080/import \
		-F "file=@test.csv" \
		-H "Content-Type: multipart/form-data"

.PHONY: testGetGameSales
# Test the /getGameSales endpoint
testGetGameSales:
	@time curl http://localhost:8080/getGameSales

.PHONY: testGetGameSalesByDuration
# Test the /getGameSales endpoint with duration, eg: curl -v GET http://localhost:8080/getGameSales\?fromDate\=2021-10-09T00:00:00Z\&toDate\=2024-10-10T23:59:59Z
testGetGameSalesByDuration:
	@time curl http://localhost:8080/getGameSales\?fromDate\=2021-10-09T00:00:00Z\&toDate\=2024-10-10T23:59:59Z

.PHONY: testGetGameSalesBySalePriceComparison
# Test the /getGameSales endpoint with sale price, eg: curl -v GET http://localhost:8080/getGameSales\?salePrice\=20\&priceComparison\=less
testGetGameSalesBySalePriceComparison:
	@time curl http://localhost:8080/getGameSales\?salePrice\=20\&priceComparison\=less


.PHONY: testGetTotalSalesByDuration
# Test the /getTotalSales endpoint with duration, eg: curl -v GET http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10
testGetTotalSalesByDuration:
	@time curl http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10

.PHONY: testGetTotalSalesByDurationAndGameNo
# Test the /getTotalSales endpoint with duration and gameNo, eg: curl -v GET http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10\&gameNo\=50
testGetTotalSalesByDurationAndGameNo:
	@time curl http://localhost:8080/getTotalSales\?fromDate\=2024-10-09\&toDate\=2024-10-10\&gameNo\=50


.PHONY: gamesalesConnector
# Set the mysql connection
gamesalesConnector:
	@curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @mysql-connector.json

.PHONY: help
# Help.
help:
	@echo ''
	@echo 'Usage:'
	@echo ' make [target]'
	@echo ''
	@echo 'Targets:'
	@awk '/^[a-zA-Z\-\0-9]+:/ { \
	helpMessage = match(lastLine, /^# (.*)/); \
		if (helpMessage) { \
			helpCommand = substr($$1, 0, index($$1, ":")-1); \
			helpMessage = substr(lastLine, RSTART + 2, RLENGTH); \
			printf " - \033[36m%-20s\033[0m %s\n", helpCommand, helpMessage; \
		} \
	} \
	{ lastLine = $$0 }' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help