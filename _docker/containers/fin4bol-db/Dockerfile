FROM --platform=linux/x86_64 postgres:latest
ENV TZ=Europe/Amsterdam
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV POSTGRES_MAX_CONNECTIONS=1024
