FROM openjdk:17
COPY ./target/Airports-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]
EXPOSE 8080
