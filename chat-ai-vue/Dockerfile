# 构建阶段
FROM node:18-alpine AS builder

WORKDIR /app

# 配置淘宝镜像源（同时设置npm和yarn源）
RUN yarn config set registry https://registry.npmmirror.com && \
    npm config set registry https://registry.npmmirror.com

# 优先复制包管理相关文件（利用缓存层）
COPY package.json yarn.lock ./
COPY postcss.config.js tailwind.config.js ./

# 安装依赖（使用国内源加速）
RUN yarn install --frozen-lockfile --network-timeout 300000

# 复制其他项目文件（排除node_modules）
COPY . .

# 构建步骤
RUN yarn build

# 生产阶段
FROM nginx:stable-alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY vue-nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]