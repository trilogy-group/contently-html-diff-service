# HTML Diff Service

[![CircleCI](https://circleci.com/gh/contently/html-diff-service.svg?style=svg)](https://circleci.com/gh/contently/html-diff-service)

The HTML Diff Service is a thin wrapper around [DaisyDiff](https://github.com/DaisyDiff/DaisyDiff) project. It provides a basic interface to support visual comparison of HTML.

## Usage
Included in the project is a Dockerfile, which is the recommended way to use it.

## Architecture decisions

The decisions for this service are recorded as [architecture decision records](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions) in the [project repository](doc/architecture/decisions). They are managed with [adr-tools](https://github.com/npryce/adr-tools).


## Development
The service is built leveraging Spring Boot with Maven on Java 8. While not necessarily a _prerequisite_, the [ASDF version manager](https://github.com/asdf-vm/asdf) to manage language depedencies. If installed, necessary dependencies can be installed by running the following command:

```shell
./developer_setup.sh
```

The application can be started via Spring Boot and Maven:

```shell
mvn spring-boot:run
```
