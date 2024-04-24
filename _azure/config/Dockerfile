FROM --platform=linux/amd64 openjdk:21
LABEL maintainer="Ahmed Aziz"
LABEL description="Fin4bol engine Docker in Azure"
EXPOSE 80

RUN groupadd -g 999 ahmed && useradd -r -u 999 -g ahmed ahmed
RUN mkdir /application && chown ahmed:ahmed /application

USER ahmed

COPY /application/finance4bol.jar /application/finance4bol.jar

WORKDIR /application

ENTRYPOINT ["java", "-Duser.country=NL", "-Duser.language=nl", "-Duser.timezone=Europe/Amsterdam", "-Dspring.profiles.active=azure", "-Dserver.port=80", "-jar", "./finance4bol.jar"]
