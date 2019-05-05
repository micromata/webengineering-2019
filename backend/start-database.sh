#!/bin/bash

echo "Starting database"
docker run --rm --name backend-postgres -e POSTGRES_PASSWORD=docker -p 5433:5432 \
    -v $(pwd)/database:/var/lib/postgresql/data postgres