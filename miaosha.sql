﻿# Host: 192.168.136.136  (Version: 5.7.26-0ubuntu0.18.04.1)
# Date: 2019-06-17 23:13:44
# Generator: MySQL-Front 5.3  (Build 4.269)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "goods"
#

DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
  `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) DEFAULT NULL COMMENT '商品的图片',
  `goods_detail` longtext COMMENT '商品的详情介绍',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "goods"
#

INSERT INTO `goods` VALUES (1,'iphoneX','Apple iPhoneX(A1865) 64GB 银色 移动联通电信4G手机','/img/iphonex.png','Apple iPhoneX(A1865) 64GB 银色 移动联通电信4G手机',8765.00,10000),(2,'华为Mate9','华为 Mate9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待','/img/mate10.png','华为 Mate9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待',3212.00,-1);

#
# Structure for table "miaosha_goods"
#

DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `miaosha_price` decimal(10,2) DEFAULT '0.00' COMMENT '秒杀价',
  `stock_count` int(11) DEFAULT NULL COMMENT '商品库存',
  `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "miaosha_goods"
#

INSERT INTO `miaosha_goods` VALUES (1,1,0.01,9,'2019-06-16 15:17:00','2019-06-17 21:00:18'),(2,2,0.01,8,'2019-06-17 14:00:14','2019-06-18 14:00:24');

#
# Structure for table "miaosha_order"
#

DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

#
# Data for table "miaosha_order"
#

INSERT INTO `miaosha_order` VALUES (1,18913243456,1,1),(2,15825252522,2,2),(3,12342342234,3,2);

#
# Structure for table "miaosha_user"
#

DROP TABLE IF EXISTS `miaosha_user`;
CREATE TABLE `miaosha_user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID，手机号码',
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "miaosha_user"
#

INSERT INTO `miaosha_user` VALUES (12342342234,NULL,'e5d22cfc746c7da8da84e0a996e0fffa','1a2b3c4d',NULL,'2019-06-17 21:08:05',NULL,0),(15825252522,NULL,'b7797cce01b4b131b433b6acf4add449','1a2b3c4d',NULL,'2019-06-17 20:22:22',NULL,0),(18913243456,'lahhass','b7797cce01b4b131b433b6acf4add449','1a2b3c4d',NULL,'1988-01-01 01:02:03','2019-05-02 09:03:04',1);

#
# Structure for table "order_info"
#

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址ID',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int(11) DEFAULT '0' COMMENT '商品数量',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `order_channel` tinyint(4) DEFAULT '0' COMMENT '1pc, 2android, 3ios',
  `status` tinyint(4) DEFAULT '0' COMMENT '订单状态,0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

#
# Data for table "order_info"
#

INSERT INTO `order_info` VALUES (1,18913243456,1,NULL,'iphoneX',1,0.01,1,0,'2019-06-16 21:02:47',NULL),(2,15825252522,2,NULL,'??Mate9',1,0.01,1,0,'2019-06-17 20:24:27',NULL),(3,12342342234,2,NULL,'??Mate9',1,0.01,1,0,'2019-06-17 21:08:28',NULL);
