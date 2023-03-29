.PHONY: build
build:
	./gradlew build

test:
	bin/test.sh

submit:
	bin/submit.sh

run:
	bin/run.sh $(JOB)
