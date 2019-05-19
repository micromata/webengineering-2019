# See https://nodejs.org/de/docs/guides/nodejs-docker-webapp/
FROM node:8
WORKDIR /app

COPY package.json /app
RUN npm install

# TODO ML Use dedicated directory for resources
COPY . .
RUN sed -i "s|.*BACKEND|const backend='https://web-news-backend.herokuapp.com';|" main.js

CMD ["node", "server.js"]