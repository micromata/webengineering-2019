#!/usr/bin/env bash

runs=100

for i in $(seq 1 ${runs})
do
    echo -n "RUN $i: "
    n=$(($RANDOM % 100))
    # TODO ML Add variables for easier configuration.
    if [[ ${n} -lt 10 ]]
    then
        echo "Creating a new post..."
        http POST :8080/api/post title="Title $RANDOM" url="https://en.wikipedia.org/wiki/Special:Random" >/dev/null
    else
        echo -n "Creating a comment..."
        n=$(($RANDOM % 100))
        if [[ ${n} -lt 30 ]]
        then
            # Create comment to post for comment
            postid=$(\
                http GET :8080/api/debug/all |\
                jq -r '.[] | select (.url == null) | .id' |\
                sort -R |\
                head -n 1 |\
                cut -d' ' -f1)
            http -v POST :8080/api/comment/${postid}/comment comment="Comment/Comment $RANDOM" >/dev/null
            echo "for a comment"
        else
            # Create comment to post for post
            postid=$(\
                http GET :8080/api/debug/all |\
                jq -r '.[] | select (.url != null) | .id' |\
                sort -R |\
                head -n 1 |\
                cut -d' ' -f1)
            http -v POST :8080/api/post/${postid}/comment comment="Comment/Post $RANDOM" >/dev/null
            echo "for a post"
        fi
    fi
done
