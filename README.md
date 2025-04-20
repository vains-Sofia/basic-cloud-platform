<h2 style="margin: 30px 0 30px; text-align: center; font-weight: bold;">Basic Cloud Platform</h2>
<h4 style="text-align: center;">围绕Spring OAuth2 Authorization Server开发的Spring Cloud项目，目前还有很多东西不完善。</h4>

---
<div style="text-align: center">

[![Static Badge](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?logo=springboot)](https://docs.spring.io/spring-boot/index.html)
[![Static Badge](https://img.shields.io/badge/Spring%20OAuth2%20Authorization%20Server-1.4.2-6DB33F?logo=springsecurity)](https://docs.spring.io/spring-authorization-server/reference/index.html)
[![Static Badge](https://img.shields.io/badge/Spring%20Cloud-2024.0.1-6DB33F?logo=spring)](https://docs.spring.io/spring-cloud-release/reference/index.html)
[![Static Badge](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2023.0.3.2-ff6a00?logo=alibabacloud)](https://sca.aliyun.com/)
[![Static Badge](https://img.shields.io/badge/Spring%20Doc-2.8.6-6ba43a)](https://springdoc.org/)
[![Static Badge](https://img.shields.io/badge/Mybatis%20Plus-3.5.11-1e90ff)](https://baomidou.com/)
[![Static Badge](https://img.shields.io/badge/Spring%20Boot%20Admin-3.4.5-42d3a5)](https://docs.spring-boot-admin.com/3.4.5/docs/index)
[![Static Badge](https://img.shields.io/badge/Java-21%2B-c74634?logo=openjdk)](https://www.graalvm.org/)
[![Static Badge](https://img.shields.io/badge/Nacos-2.5.1-1be1f6)](https://nacos.io)
[![Static Badge](https://img.shields.io/badge/Apache%20Maven-3.9.9-f5f5f5?logo=apachemaven)](https://maven.apache.org/)
[![Static Badge](https://img.shields.io/badge/License-Apache%20License%202.0-f5f5f5?logo=apache)](./LICENSE)
![Static Badge](https://img.shields.io/badge/Author-vains_Sofia(%E4%BA%91%E9%80%B8)-blue)

</div>

----

### 平台简介

&emsp;&emsp;该项目主要是为了学习Spring Cloud和Spring OAuth2 Authorization Server而开发的，熟悉Spring Cloud项目从开发到部署的一个完整流程，目前尚未开发完毕。

### 主要功能

 - 认证中心
 - 网关
 - 监控中心
 - 系统服务

### 项目结构
```shell
|-- basic-cloud-platform
    |-- pom.xml -- 根pom.xml
    |-- basic-examples -- 示例项目
    |   |-- basic-example-data-jpa -- Jpa封装使用示例
    |   |-- basic-example-doc -- SpringDoc封装使用示例
    |   |-- basic-example-mybatis-plus -- Mybatis-Plus封装使用示例
    |   |-- basic-example-redis -- Redis封装使用示例
    |   |-- basic-example-resource-server -- 资源服务器使用示例
    |-- basic-framework
    |   |-- basic-framework-core -- framework核心包
    |   |-- basic-framework-data-jpa -- Spring Data Jpa封装，实体统一父类，自动设置审计信息，统一枚举转换
    |   |-- basic-framework-data-redis -- Spring Data Redis封装，RedisTemplate封装，Redis分布式锁封装
    |   |-- basic-framework-data-validation -- Spring Data Validation封装，统一参数校验
    |   |-- basic-framework-mybatis-plus -- Mybatis-Plus封装，统一分页查询，统一枚举转换
    |   |-- basic-framework-oauth2 -- 认证相关framework
    |   |   |-- basic-framework-oauth2-authorization-server -- 认证服务封装，扩展登录、Grant Type、Token、Client等
    |   |   |-- basic-framework-oauth2-core -- 认证核心模块封装
    |   |   |-- basic-framework-oauth2-federation -- 联合身份认证封装，支持三方登录
    |   |   |-- basic-framework-oauth2-resource-server -- 资源服务器封装，同时支持JWT、Opaque Token
    |   |   |-- basic-framework-oauth2-storage -- 认证中心存储实现，目前基于db存储
    |   |   |-- basic-framework-oauth2-storage-old -- 认证中心存储实现，支持多种存储，弃用
    |   |-- basic-framework-openfeign -- 远程调用封装，FeignClient支持RequestMapping注解
    |   |-- basic-framework-spring-doc -- Spring Doc封装，支持展示枚举、使用认证中心登录等
    |   |-- basic-framework-web -- Spring Web封装，支持统一异常处理、统一返回结果、统一参数校验
    |-- basic-services -- 项目微服务模块
    |   |-- basic-service-authorization-server -- Spring OAuth2 Authorization Server认证中心服务
    |   |-- basic-service-gateway -- Spring Cloud Gateway网关服务
    |   |-- basic-service-monitor -- Spring Boot Admin监控服务
    |   |-- basic-service-system -- 系统服务，包含用户、角色、权限等
    |-- basic-services-api -- FeignClient远程调用api模块
    |   |-- basic-service-system-api -- 系统服务相关 API
    |-- docs -- 文档相关
        |-- deploy -- 部署相关
        |   |-- Centos
        |   |   |-- deploy.sh -- 服务器部署脚本
        |   |-- docker
        |   |   |-- Dockerfile -- 服务部署至docker时打包使用的Dockerfile
        |   |   |-- infra
        |   |       |-- infra-compose.yml -- 服务依赖的组件，如数据库、Nacos等
        |   |-- yaml-backup -- 备份的yaml文件
        |       |-- application-authorization-server.yml
        |-- nacos
        |   |-- nacos_config_dev.zip -- nacos配置中心dev环境配置
        |   |-- nacos_config_test.zip -- nacos配置中心test环境配置
        |-- sql -- 项目数据库相关
            |-- basic-cloud-platform-dev.sql -- 项目数据库脚本
            |-- basic-cloud-platform-test.sql -- 项目测试数据库脚本
```

### 本地启动说明
1. 克隆项目
```shell
git clone https://gitee.com/vains-Sofia/basic-cloud-platform.git
```
2. 初始化所需组件
<br />将`docs/deploy/docker/infra`下的infra-compose.yml上传至测试服务器，然后执行Docker Compose命令初始化组件
```shell
  docker-compose -f infra-compose.yml up -d
```
&emsp;&emsp;或者直接进入目录下执行(如果已安装Docker)
```shell
cd docs/deploy/docker/infra
docker-compose -f infra-compose.yml up -d
```

3. 创建数据库
创建数据库
```sql
create database basic-cloud-platform character set utf8mb4 collate utf8mb4_bin;
```

4. 导入数据库脚本
<br />
导入`docs/sql/basic-cloud-platform-dev.sql`脚本至刚才创建的数据库中
5. 导入Nacos配置
<br />
导入`docs/nacos/nacos_config_dev.zip`至Nacos配置中心。
6. 修改nacos中各依赖组件的ip，例如MySQL、Redis等
7. 启动服务
&emsp;&emsp;依次启动basic-service-authorization-server、basic-service-gateway、basic-service-monitor、basic-service-system服务，顺序无所谓，推荐最后启动monitor服务。<br />
- Swagger地址：http://127.0.0.1:9000/swagger-ui/index.html
- Spring Boot Admin监控中心：http://127.0.0.1:9000/monitor/

邮箱登录：
邮箱：17683906991@163.com
验证码：随便