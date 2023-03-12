## Postgres Docker Setup

Run `./postgres-docker-run.sh` to create and start the container. This creates the container `postgres-instance`, sets the default user to `postgres` with the password to `123` and runs on port `5432`.

## Starting Postgres from Docker

You can start the container with `./postgres-docker-start.sh`.

## Stoping Postgres from Docker

You can stop the container with `./postgres-docker-stop.sh`.


## Troubleshooting

If you plan on accessing the postgres database from pgadmin which is also running in docker you likely will need the container host name in order to connect with the database.

Run `docker inspect postgres-instance | grep IPAddress` or `docker inspect postgres-instance` to find:
```
"IPAddress": "xxx.xx.x.x",
    "IPAddress": "xxx.xx.x.x",
```

This will be the host for you postgres database when connecting with pgadmin.
