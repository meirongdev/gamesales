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