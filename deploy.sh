#!/usr/bin/env bash

# Stop on error
set -e

if [ -n "$1" ]
then
    apps=( $1 )
else
    apps=(backend frontend)
fi

for name in "${apps[@]}"
do
    echo "*** Deploying $name ***"
    cd $name

    if [[ -e build.sh ]]
    then
        ./build.sh
    fi

    heroku container:push web -a web-news-$name
    heroku container:release web -a web-news-$name
    cd ..
done