#!/usr/bin/env bash

# You need the tools httpie and jq installed to execute this script. Please find out by yourself how to install them
# since it varies widely by operating system.

echo "Deleting database..."
http DELETE :8080/api/post

size=10
echo "Adding ${size} entries"
for i in $(seq 1 $size)
do
    title=$(http https://api.kanye.rest/|jq .quote)
    http POST :8080/api/post title="${title}"
done