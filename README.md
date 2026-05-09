# 校园小卖部小程序

一个完整的校园小卖部解决方案，包含微信小程序、后端服务和管理后台。

## 项目结构

```
Dorm_Kiosk/
├── client/          # 管理后台（Vue3）
├── server/          # 后端服务（Spring Boot）
└── weixin/          # 微信小程序
```

## 功能模块

### 小程序端
- ✅ 用户登录/注册
- ✅ 商品浏览（分类、搜索）
- ✅ 购物车管理
- ✅ 下单支付
- ✅ 订单管理
- ✅ 收货地址管理

### 管理后台
- ✅ 商品管理
- ✅ 分类管理
- ✅ 订单管理
- ✅ 用户管理
- ✅ 数据统计

### 后端服务
- ✅ 用户认证（JWT）
- ✅ 商品API
- ✅ 购物车API
- ✅ 订单API
- ✅ 支付接口

## 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- MyBatis Plus
- MySQL
- Redis

### 前端（管理后台）
- Vue 3
- Vite
- Element Plus
- Pinia

### 小程序
- 原生微信小程序
- TypeScript

## 快速开始

### 1. 后端服务

```bash
cd server
# 修改 application.yml 中的数据库配置
# 运行 schema.sql 初始化数据库
mvn spring-boot:run
```

### 2. 管理后台

```bash
cd client
npm install
npm run dev
```

### 3. 微信小程序

1. 使用微信开发者工具打开 `weixin/` 目录
2. 修改 `project.config.json` 中的 appid
3. 修改 `utils/request.js` 中的后端地址
4. 编译运行

## 配置说明

复制配置示例文件并填入真实配置：

```bash
cp server/src/main/resources/application.example.yml server/src/main/resources/application.yml
```

主要配置项：
- 数据库连接
- Redis 配置
- JWT 密钥
- 微信支付配置（可选）

## 数据库

运行 `server/src/main/resources/schema.sql` 初始化数据库表结构。

默认管理员账号：
- 用户名：admin
- 密码：admin123

## 开发指南

### 后端开发

- API 文档：http://localhost:8080/swagger-ui.html
- 端口：8080

### 小程序开发

- 小程序文档：https://developers.weixin.qq.com/miniprogram/dev/framework/
- 后端地址配置：`weixin/miniprogram/utils/request.js`

## 项目截图

（待添加）

## 许可证

MIT License
