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
        echo -n "Creating a comment..."
        n=$(($RANDOM % 100))
        if [[ ${n} -lt 50 ]]
        then
            # Create comment to post for comment
            postid=$(\
                http GET :8080/api/debug/all |\
                jq -r '.[] | (.id|tostring) + " " + .content' |\
                grep 'Comment' |\
                sort -R |\
                head -n 1 |\
                cut -d' ' -f1)
            http -v POST :8080/api/comment/${postid}/comment content="Comment/Comment $RANDOM" >/dev/null
            echo "for a comment"
        else
            # Create comment to post for post
            postid=$(\
                http GET :8080/api/debug/all |\
                jq -r '.[] | (.id|tostring) + " " + .content' |\
                grep -v 'Comment' |\
                sort -R |\
                head -n 1 |\
                cut -d' ' -f1)
            http -v POST :8080/api/post/${postid}/comment content="Comment/Post $RANDOM" >/dev/null
            echo "for a post"
        fi
    fi
done
