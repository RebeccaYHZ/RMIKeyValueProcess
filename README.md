# RMI Key-Value Store Project

## Overview

This project implements a remote method invocation (RMI) key-value store. The server allows clients to perform operations such as adding, retrieving, and deleting key-value pairs in a concurrent environment.

1. PUT (key, value)
2. GET (key)
3. DELETE (key)

## Author

Yahui Zhang

## Prerequisites

Before you begin, ensure you have the following installed:

- Docker - For containerization

## Usage

1. **Building the Docker Image**
   To build the Docker image for the project, navigate to the root directory of the project where the Dockerfile is located and run:

```bash
docker build -t java-rmi-server .
```

2. **Running the RMI Server**:
   To run the RMI server, use the following command:

```bash
docker run --network="host" -it --entrypoint /bin/sh java-rmi-server
java -cp bin server.RMIServer &
```

If success, you will see "RMI server is running and KeyValueService is bound."

3. **Running the RMI Client**:
   To run the RMI client, use the following command:

```bash
java -cp bin client.RMIClient localhost 1099
```

4. **End running**:
   To end running, use the following command:

```bash
exit
```
