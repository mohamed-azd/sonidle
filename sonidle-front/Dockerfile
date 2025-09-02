FROM node:22-alpine AS build

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000
ENV NUXT_PUBLIC_API_BASE_URL=http://localhost/backend-api
ENV NUXT_PUBLIC_WS_URL=http://localhost/backend-api/ws

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . ./
RUN npm run build

FROM node:22-alpine
WORKDIR /app
COPY --from=build /app/.output/ ./
EXPOSE 3000
CMD ["node", "/app/server/index.mjs"]