## PGAdmin Docker Setup

Run `./pgadmin-docker-run.sh` to create and start the container. This creates the container `gpadmin-instance`, sets the default email to `name@gmail.com` with the password to `admin` and runs on port `80`.

## Starting PGAdmin from Docker

You can start the container with `./pgadmin-docker-start.sh`.

## Stoping PGAdmin from Docker

You can stop the container with `./pgadmin-docker-stop.sh`.


## Troubleshooting

If you cannot access pgadmin from `localhost:80`,

Run `docker inspect postgres-instance | grep IPAddress` or `docker inspect postgres-instance` to find:
```
"IPAddress": "xxx.xx.x.x",
    "IPAddress": "xxx.xx.x.x",
```

Try using the ip found from the above command, and then see the postgres README for how to connect.
