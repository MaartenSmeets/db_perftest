#Cleanup:
docker-compose stop
docker-compose rm -v -f
docker volume rm postgres_pgadmin
docker volume rm postgres_postgres
