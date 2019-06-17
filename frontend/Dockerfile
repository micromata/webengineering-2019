# See https://nodejs.org/de/docs/guides/nodejs-docker-webapp/
FROM node:8
WORKDIR /app

# Install express
COPY package.json .
RUN npm install

COPY server.js .
COPY dist dist

CMD ["node", "server.js"]