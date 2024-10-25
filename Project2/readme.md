# RMI Key-Value Store Project

## Overview
This project implements a remote method invocation (RMI) key-value store. The server allows clients to perform operations such as adding, retrieving, and deleting key-value pairs in a concurrent environment.
1) PUT (key, value)
2) GET (key)
3) DELETE (key)

## Author
Yahui Zhang


## Usage

1. **Compile the code**:
   To compile the project, navigate to the src directory in your terminal and run the following command:
```bash
javac server/*.java client/*.java
```
2. **Running the RMI Server**:
To start the RMI server, execute the following command:
```bash
java server.RMIServer
```
This will start the RMI server, and you should see a message indicating that the server is running and that the KeyValueService is bound.

3. **Running the RMI Client**:
Open a new terminal window and navigate to the src directory again. To run the RMI client, use the following command:
```bash
java client.RMIClient localhost 1099
```
4. **Process to the map**:
```bash
PUT myKey myValue
GET myKey
DELETE myKey
```

