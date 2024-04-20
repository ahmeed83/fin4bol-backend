FROM openjdk:21
RUN mkdir /application
WORKDIR /application
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "fin4bol.jar"]