FROM openjdk:21
RUN mkdir /application
WORKDIR /application
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "finance4bol.jar"]