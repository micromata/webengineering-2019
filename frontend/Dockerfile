# See https://nodejs.org/de/docs/guides/nodejs-docker-webapp/
FROM node:8
WORKDIR /app

COPY dist .
COPY server.js .

CMD ["node", "server.js"]