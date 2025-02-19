FROM openjdk:17-jdk-slim

EXPOSE 8080

ADD target/FinanceTracker-BackEnd-0.0.1-SNAPSHOT.jar  FinanceTracker-BackEnd-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java","-jar","/FinanceTracker-BackEnd-0.0.1-SNAPSHOT.jar" ]
