FROM node:18.20.4-alpine as builder

RUN mkdir -p /app

WORKDIR /app

COPY . .

RUN npm install
RUN npm run build

EXPOSE 4200

CMD ["npm", "run", "start_docker"]
