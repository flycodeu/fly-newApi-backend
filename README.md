<h1 align="center">飞云API接口开放平台</h1>
<p align="center"><strong>飞云API接口开放平台是一个为用户和开发者提供全面API接口调用服务的平台 🛠</strong></p>
<div align="center">
<a target="_blank" href="https://github.com/qimu666/qi-api">
    <img alt="" src="https://github.com/qimu666/qi-api/badge/star.svg?theme=gvp"/>
</a>
<a target="_blank" href="https://github.com/flybase1/fly-newApi-backend"></a>
</a>
    <img alt="Maven" src="https://raster.shields.io/badge/Maven-3.8.1-red.svg"/>
<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
</a>
    <img alt="SpringBoot" src="https://raster.shields.io/badge/SpringBoot-2.7+-green.svg"/>
<a href="https://github.com/flybase1/fly-newApi-backend" target="_blank"><img src='https://github.com/flybase1/fly-newApi-backend' alt='GitHub stars' class="no-zoom">
</a>
</div>

##  项目介绍
用户可以注册登录，开通接口调用的权限。用户可以使用接口，并且每次调用接口会进行统计。管理员可以发布接口，下线接口，接入接口，以及可视化接口调用的情况。
作为开发者可以使用[客户端SDK](https://github.com/flybase1/flyapi-client-sdk)， 通过填入相关的密钥ak,sk即可将轻松集成接口到您的项目中，实现更高效的开发和调用。
| 目录                                                         | 描述               |
| ------------------------------------------------------------ | ------------------ |
| **🏘️ [fly-Api-backend-user](./fly-Api-backend-user)**         |飞云API后端服务模块 |
| **🏘️ [fly-Api-common](./fly-Api-common)**                     | 公共服务模块       |
| **🕸️ [fly-Api-gateway](./fly-Api-gateway)**                    | 网关模块           |
| **🔗 [fly-Api-interface](./fly-Api-interface)**               | 接口模块           |
| **🛠 [飞云SDK]([https://github.com/qimu666/qi-api-sdk](https://github.com/flybase1/flyapi-client-sdk))**    | 开发者调用sdk      |


### 前端

环境要求：Node.js >= 16

安装依赖：

```bash
yarn or  npm install
```

启动：

```bash
yarn run dev or npm run start:dev
```

部署：

```bash
yarn build or npm run build
```

### 后端


## 项目选型 🎯

### **后端**

- Spring Boot 2.7.0
- Spring MVC
- MySQL 数据库
- Dubbo 分布式（RPC、Nacos）
- Spring Cloud Gateway 微服务网关
- API 签名认证（Http 调用）
- IJPay-AliPay  支付宝支付
- Swagger + Knife4j 接口文档
- Spring Boot Starter（SDK 开发）
- Spring Session Redis 分布式登录
- Apache Commons Lang3 工具类
- MyBatis-Plus 及 MyBatis X 自动生成
- Hutool、Apache Common Utils、Gson 等工具库

### 前端

- React 18

- Ant Design Pro 5.x 脚手架

- Ant Design & Procomponents 组件库

- Umi 4 前端框架

- OpenAPI 前端代码生成

### 界面展示
展示接口
![image-20230930144643289](http://cdn.flycode.icu/img/202309301446434.png)
调用接口
![image-20230930144712876](http://cdn.flycode.icu/img/202309301447962.png)
![image-20230930144729883](http://cdn.flycode.icu/img/202309301447950.png)
查看自己私钥
![image-20230930144805931](http://cdn.flycode.icu/img/202309301448011.png)
查看自己订单
![image-20230930144832849](http://cdn.flycode.icu/img/202309301448944.png)
管理员面板
![image-20230930144855890](http://cdn.flycode.icu/img/202309301448996.png)
  

