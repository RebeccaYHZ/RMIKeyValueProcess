FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
# RUN javac Project2/src/server/*.java Project2/src/client/*.java
RUN mkdir -p bin && \
    javac -d bin Project2/src/server/*.java Project2/src/client/*.java
EXPOSE 1099
CMD ["java", "-cp", "bin", "server.RMIServer"]