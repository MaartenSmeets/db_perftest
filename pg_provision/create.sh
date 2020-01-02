docker-compose up -d
sleep 10
docker exec -u pgadmin:pgadmin -it pgadmin_container mkdir -m 700 /var/lib/pgadmin/storage/pgadmin4_pgadmin.org
docker cp pgpassfile pgadmin_container:/tmp/pgpassfile
docker exec -it -u root pgadmin_container chown pgadmin:pgadmin /tmp/pgpassfile
docker exec -it pgadmin_container mv /tmp/pgpassfile /var/lib/pgadmin/storage/pgadmin4_pgadmin.org
docker exec -it pgadmin_container chmod 600 /var/lib/pgadmin/storage/pgadmin4_pgadmin.org/pgpassfile
docker cp servers.json pgadmin_container:/tmp/servers.json
docker exec -it pgadmin_container python /pgadmin4/setup.py --load-servers /tmp/servers.json
