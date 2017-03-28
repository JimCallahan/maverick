TAG = $(shell git rev-parse --short HEAD)
NAME = maverick
IMAGE = $(NAME):$(TAG)

all: container

container: lein
	@echo "------------------------------------"
	@echo "  Rebuilding Docker Container"
	@echo "------------------------------------"
	docker build --no-cache --force-rm -t $(IMAGE) .

lein:
	@echo "------------------------------------"
	@echo "  Rebuilding CSS"
	@echo "------------------------------------"
	cd .. && lein garden once
	@echo
	@echo "------------------------------------"
	@echo "  Rebuilding Uberjars"
	@echo "------------------------------------"
	cd .. && lein ring uberjar
	@echo
	@echo "------------------------------------"
	@echo "  Copying Uberjar" 
	@echo "------------------------------------"
	cp ../target/$(NAME).jar $(NAME).jar
	@echo
	@echo "------------------------------------"
	@echo "  Copying Dependency JARs"
	@echo "------------------------------------"
	cd .. && lein libdir

run:
	@echo "------------------------------------"
	@echo "  Running Docker Container"
	@echo "------------------------------------"
	@echo "URL: http://localhost:8080"
	docker run -p 127.0.0.1:8080:3000 $(IMAGE) start-maverick

run-jar:
	@echo "------------------------------------"
	@echo "  Running JARs"
	@echo "------------------------------------"
	java -cp 'jars/*:maverick.jar' maverick.core.main

clean:
	@echo "------------------------------------"
	@echo "  Cleaning Up..."
	@echo "------------------------------------"
	cd .. && lein clean
	rm -rf jars
	rm -f $(NAME).jar
	docker rmi -f $(IMAGE)

.PHONY: lein 
