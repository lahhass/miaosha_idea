## Spring Boot高并发秒杀项目
## 详细介绍：https://blog.csdn.net/baidu_25104885/column/info/40503
## 开发工具
IDEA 2017.1.2
## 项目环境
JDK|Spring Boot|MySQL|Redis|RabbitMQ
--|--|--|--|--
1.8|1.5.9.RELEASE|5.7.26|5.0.4|3.7.15
## 项目亮点
1. 实现了分布式Session，分布式的多台应用服务器都能响应请求
2. 使用redis做各类缓存，减轻数据库压力，提高并发量和访问速度，秒杀时内存标记减少redis访问
3. 页面静态化，将静态页面缓存在浏览器，提高页面加载速度，也减轻服务器压力
4. 使用RabbitMQ消息队列异步下单，增强用户体验，流量削峰
5. 安全优化： 秒杀接口地址隐藏、数学公式图形验证码（也是削峰重要手段）、 接口防刷限流

## 运行说明
1. 安装redis、mysql、rabbitmq等环境
2. MySQL-Front/Navicate输入sql文件
3. 到src/main/resources下的application.properties下修改相关redis、mysql、rabbitmq的地址、用户名、密码等信息
4. 在数据库秒杀商品表里面设置合理的秒杀开始时间与结束时间
5. 注册地址：http://localhost:8083/register/to_register
6. 登录地址：http://localhost:8083/login/to_login
7. 商品秒杀列表地址：http://localhost:8083/goods/to_list
8. application.properties里可指定端口

## 运行演示

用户注册
![用户注册](https://github.com/lahhass/pictures/blob/master/%E6%B3%A8%E5%86%8C.PNG?raw=true)


用户登录
![用户登录](https://github.com/lahhass/pictures/blob/master/%E7%99%BB%E5%BD%95.PNG?raw=true)


秒杀商品列表
![秒杀商品列表](https://github.com/lahhass/pictures/blob/master/%E5%95%86%E5%93%81%E5%88%97%E8%A1%A8.PNG?raw=true)


秒杀倒计时
![秒杀倒计时](https://github.com/lahhass/pictures/blob/master/%E7%A7%92%E6%9D%80%E7%95%8C%E9%9D%A2.PNG?raw=true)


秒杀成功       
![秒杀成功](https://github.com/lahhass/pictures/blob/master/%E6%88%90%E5%8A%9F.PNG?raw=true)


订单详情
![订单详情](https://github.com/lahhass/pictures/blob/master/%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85%E9%A1%B5.PNG?raw=true)


秒杀已结束
![秒杀已结束](https://github.com/lahhass/pictures/blob/master/%E7%BB%93%E6%9D%9F.PNG?raw=true)
