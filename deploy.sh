#!/usr/bin/env bash

# Stop on error
set -e

apps=(backend frontend)
for name in "${apps[@]}"
do
    echo "*** Deploying $name ***"
    cd $name
    heroku container:push web -a web-news-$name
    heroku container:release web -a web-news-$name
    cd ..
done