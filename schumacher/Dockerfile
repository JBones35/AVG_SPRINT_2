FROM node:23 AS builder

LABEL maintainer="Jonas Kirschner <kirschner.jonas@gmx.de>"
LABEL description="Build-Stage für NestJS Anwendung"

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build

FROM node:23-alpine AS production

LABEL maintainer="Jonas Kirschner <kirschner.jonas@gmx.de>"
LABEL description="Production-Stage für NestJS Anwendung"

WORKDIR /usr/src/app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY package*.json ./

RUN npm ci --omit=dev --ignore-scripts

COPY --chown=appuser:appgroup --from=builder /usr/src/app/dist ./dist

EXPOSE 3000

USER appuser

CMD ["node", "dist/main.js"]