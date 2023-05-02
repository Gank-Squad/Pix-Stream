docker run \
   --name pgadmin-instance \
   -p 5050:80 \
   -e "PGADMIN_DEFAULT_EMAIL=name@gmail.com" \
   -e "PGADMIN_DEFAULT_PASSWORD=admin" \
   -d dpage/pgadmin4
