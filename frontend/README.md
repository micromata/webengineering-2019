# Overview

This is the frontend for our application. Let's start with a simple [https://expressjs.com/](express)-based applictation to  serve our static content. This might be oversized (for simply serving static files) but we want to start fast and easy. Think about using _the right tool for the right job_ but also _what's the simplest working approach_?.

We start by generating a `package.json` file using this [http://jspkg.com/generator](generator). 

To add the express dependency, we call `npm install express --save`.

The file server can be started with

    node server.js
    
# Persistent database

Start a postgres container in docker using 

    docker run --rm --name backend-postgres -e POSTGRES_PASSWORD=docker -p 5433:5432 \
        -v $(pwd)/database:/var/lib/postgresql/data postgres     
        
Note that you have to adapt the command line for windows. Be aware that we use the non-standard postgres port `5433` 
instead of 5432 to prevent port conflicts with a possible already running postgres database on your system.         
        