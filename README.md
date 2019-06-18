# miaosha_idea
Java高并发秒杀项目
## 开发工具
IDEA 2017.1.2
## 项目环境
JDK|Spring Boot|MySQL|Redis|RabbitMQ
--|--|--|--|--
1.8|1.5.9.RELEASE|5.7.26|5.0.4|3.7.15
## 运行说明
1. 安装redis、mysql、rabbitmq等环境
2. MySQL-Front/Navicate输入sql文件
3. 到src/main/resources下的application.properties下修改相关redis、mysql、rabbitmq的地址、用户名、密码等信息
4. 在数据库秒杀商品表里面设置合理的秒杀开始时间与结束时间
5. 注册地址：http://localhost:8083/register/to_register
6. 登录地址：http://localhost:8083/login/to_login
7. 商品秒杀列表地址：http://localhost:8083/goods/to_list
8. application.properties里可指定端口

