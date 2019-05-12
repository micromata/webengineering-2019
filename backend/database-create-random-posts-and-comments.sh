#!/usr/bin/env bash

runs=100

for i in $(seq 1 ${runs})
do
    echo -n "RUN $i: "
    n=$(($RANDOM % 100))
    if [[ ${n} -lt 10 ]]
    then
        echo "Creating a new post..."
        http POST :8080/api/post title="Title $RANDOM" content="https://en.wikipedia.org/wiki/Special:Random" >/dev/null
    else
        echo "Creating a comment..."
        postid=$(http GET :8080/api/debug/all|jq '.[] | .id' | sort -R | head -n 1)
        http -v POST :8080/api/post/${postid}/comment content="Comment $RANDOM" >/dev/null
    fi
done
