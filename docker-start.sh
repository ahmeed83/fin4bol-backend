#!/bin/sh

# stop all dockers
docker-compose -f ./_docker/application/docker-compose-it.yml stop

case $1 in
    db)
        docker-compose -f ./_docker/application/docker-compose-db.yml up -d
        ;;
    db-fresh)
        docker-compose -f ./_docker/application/docker-compose-db.yml down -v
        docker-compose -f ./_docker/application/docker-compose-db.yml up -d
        echo "Waiting for PostgresSQL to be ready..."
        until docker exec fin4bol-db pg_isready -h localhost -p 5432 > /dev/null 2>&1; do
            sleep 1
            echo -n "."
        done
        echo "PostgresSQL is ready."
        ;;
    it)
        docker-compose -f ./_docker/application/docker-compose-it.yml up
        ;;
    *)
        echo "usage docker-start [db|it]"
        echo "use:"
        echo "   'db' for starting the postgres database"
        echo "   'db-fresh' for starting fresh the postgres database"
        echo "   'it' for starting the postgres database and the fin4bol-api engine"
esac
