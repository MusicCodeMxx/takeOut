/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 5.6.50-log : Database - open_order_take_out
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`open_order_take_out` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `open_order_take_out`;

/*Table structure for table `ss_attributes` */

DROP TABLE IF EXISTS `ss_attributes`;

CREATE TABLE `ss_attributes` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性名称',
  `value` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '口味数据数组:[属性1,属性2,...]',
  `def_value_index` tinyint(100) DEFAULT '0' COMMENT '默认选择的属性值下标,-1表示没有默认值',
  `description` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性描述',
  `sort` tinyint(100) DEFAULT NULL COMMENT '顺序,越大排序最前',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='产品属性表';

/*Data for the table `ss_attributes` */

insert  into `ss_attributes`(`id`,`name`,`value`,`def_value_index`,`description`,`sort`,`update_time`,`update_by_id`,`create_time`,`create_by_id`,`is_delete`) values 
(1397854133632413697,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]',0,NULL,NULL,NULL,NULL,NULL,NULL,0),
(1397854652623007745,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]',0,'选择您忌口的口味',NULL,NULL,NULL,NULL,NULL,0),
(1397859056709316609,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]',0,NULL,NULL,NULL,NULL,NULL,NULL,0),
(1397859757061615618,'甜味','[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]',0,NULL,NULL,NULL,NULL,NULL,NULL,0),
(1397861370035744769,'忌口','[\"不要葱\",\"不要香菜\",\"不要辣\"]',0,'选择您忌口的口味',NULL,NULL,NULL,NULL,NULL,0),
(1397862477835317250,'辣度','[\"不辣\",\"微辣\",\"中辣\"]',0,NULL,NULL,NULL,NULL,NULL,NULL,0),
(1413389540592263169,'温度','[\"常温\",\"冷藏\"]',0,NULL,NULL,NULL,NULL,NULL,NULL,0);

/*Table structure for table `ss_category` */

DROP TABLE IF EXISTS `ss_category`;

CREATE TABLE `ss_category` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '分类名称',
  `sort` tinyint(100) DEFAULT '0' COMMENT '顺序,越大排序最前',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类详细表';

/*Data for the table `ss_category` */

insert  into `ss_category`(`id`,`type`,`description`,`name`,`sort`,`create_time`,`create_by_id`,`update_time`,`update_by_id`,`is_delete`) values 
(1397844263642378242,1,NULL,'湘菜',1,'2021-05-27 09:16:58',1,'2021-07-15 20:25:23',1,0),
(1397844303408574465,1,NULL,'川菜',0,'2021-05-27 09:17:07',1,'2021-06-02 14:27:22',1,0),
(1397844391040167938,1,NULL,'粤菜',0,'2021-05-27 09:17:28',1,'2021-07-09 14:37:13',1,0),
(1413341197421846529,1,NULL,'饮品',0,'2021-07-09 11:36:15',1,'2021-07-09 14:39:15',1,0),
(1413342269393674242,2,NULL,'组合套餐',0,'2021-07-09 11:40:30',1,'2022-06-21 01:14:57',1,1),
(1413384954989060097,1,NULL,'主食',0,'2021-07-09 14:30:07',1,'2021-07-09 14:39:19',1,0),
(1413386191767674881,2,NULL,'儿童套餐',0,'2021-07-09 14:35:02',1,'2021-07-09 14:39:05',1,1);

/*Table structure for table `ss_child_order_detail` */

DROP TABLE IF EXISTS `ss_child_order_detail`;

CREATE TABLE `ss_child_order_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '主订单主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品主键',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品名称',
  `value` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品属性',
  `image_def_url` varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品主图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT '产品价格',
  `amount` int(16) DEFAULT NULL COMMENT '产品购买数量',
  `category_id` bigint(20) DEFAULT NULL COMMENT '产品分类主键',
  `out_trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '订单原始价',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单详情信息表';

/*Data for the table `ss_child_order_detail` */

/*Table structure for table `ss_coupon` */

DROP TABLE IF EXISTS `ss_coupon`;

CREATE TABLE `ss_coupon` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `batch_id` bigint(20) DEFAULT NULL COMMENT '批次表主键',
  `price` decimal(10,2) DEFAULT NULL COMMENT '优惠券面额',
  `threshold` decimal(10,2) DEFAULT NULL COMMENT '消费门槛',
  `status` tinyint(1) DEFAULT '0' COMMENT '批次表状态: 0未使用, 1已使用, 2过期, 3冻结, 4异常',
  `coupon_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券名称',
  `description` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券描述',
  `start_time` datetime DEFAULT NULL COMMENT '使用起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '使用截止时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '领取该券的用户主键',
  `use_time` datetime DEFAULT NULL COMMENT '优惠券被核销(使用)时间*',
  `use_out_trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '核销(使用)该优惠券订单号',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='优惠券详情信息表';

/*Data for the table `ss_coupon` */

/*Table structure for table `ss_coupon_batch_detail` */

DROP TABLE IF EXISTS `ss_coupon_batch_detail`;

CREATE TABLE `ss_coupon_batch_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `coupon_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券名称',
  `batch_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券批次名称',
  `description` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券描述',
  `status` tinyint(1) DEFAULT '0' COMMENT '批次表状态: 0未使用, 1已使用, 2过期, 3冻结, 4异常',
  `threshold` decimal(10,2) DEFAULT NULL COMMENT '消费门槛',
  `price` decimal(10,2) DEFAULT NULL COMMENT '优惠券面额',
  `total_count` int(16) DEFAULT NULL COMMENT '优惠券总量',
  `receive_count` int(16) DEFAULT NULL COMMENT '优惠券已领取量',
  `start_time` datetime DEFAULT NULL COMMENT '使用起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '使用截止时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='优惠券批次详情信息表';

/*Data for the table `ss_coupon_batch_detail` */

insert  into `ss_coupon_batch_detail`(`id`,`coupon_name`,`batch_name`,`description`,`status`,`threshold`,`price`,`total_count`,`receive_count`,`start_time`,`end_time`,`update_time`,`update_by_id`,`create_time`,`create_by_id`,`is_delete`) values 
(120,'测试满减优惠券','测试优惠券','全场满88元减50元',1,'100.00','100.00',12,2,'2022-08-01 22:48:12','2022-08-26 23:48:17','2022-08-01 22:48:19',NULL,'2022-08-01 23:48:38',NULL,0),
(121,'优惠券','优惠券','全场满88元减50元',1,'88.00','50.00',10,4,'2022-08-01 22:47:04','2022-08-25 23:47:07','2022-08-01 23:47:11',NULL,'2022-08-01 23:47:12',NULL,0),
(122,'优惠券','优惠券','全场满60元减30元',1,'60.00','30.00',60,3,'2022-08-01 22:37:42','2022-08-30 23:37:45','2022-08-01 23:37:51',NULL,'2022-08-01 23:37:53',NULL,0),
(123,'测试满减优惠券','测试优惠券','全场满10元减1元',1,'10.00','1.00',100,23,'2022-08-01 22:13:19','2022-08-31 23:13:22','2022-08-01 23:13:26',NULL,'2022-08-01 23:13:29',NULL,0);

/*Table structure for table `ss_login_record` */

DROP TABLE IF EXISTS `ss_login_record`;

CREATE TABLE `ss_login_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '绑定用户主键ID',
  `type` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录枚举',
  `ip` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录的IP地址',
  `address` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录IP归属地址',
  `pro_code` bigint(10) DEFAULT NULL COMMENT '省级行政代码',
  `city_code` bigint(10) DEFAULT NULL COMMENT '市级行政代码',
  `way_type` tinyint(1) DEFAULT NULL COMMENT '登录方式:0密码登录,1短信登录,2微信登录',
  `browser_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录的浏览器名',
  `browser_version` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录的浏览器版本',
  `manufacturer` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录的浏览器生产厂商',
  `device_type` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录设备类型',
  `system_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录操作系统名',
  `system_group` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录设备的操作系统家族',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='登录记录表';

/*Data for the table `ss_login_record` */

/*Table structure for table `ss_order_detail` */

DROP TABLE IF EXISTS `ss_order_detail`;

CREATE TABLE `ss_order_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态, 0创建,1待付款,2后厨制作中,3制作完成待派送,4正在派送中,5已派送完成,6已完成,7已取消,8超时未支付已自动取消,9退款申请,10退款成功 ',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '订单原始价',
  `freight_price` decimal(10,2) DEFAULT NULL COMMENT '运费',
  `coupon_price` decimal(10,2) DEFAULT NULL COMMENT '优惠要减免总价',
  `product_total_number` int(16) DEFAULT NULL COMMENT '该订单产品总数',
  `bill_price` decimal(10,2) DEFAULT NULL COMMENT '优惠要减免总价',
  `out_trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `subject` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单标题',
  `body` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单内容',
  `remark` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户备注',
  `phone_number` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收货人手机号',
  `address_book_id` bigint(20) DEFAULT NULL COMMENT '地址绑定的主键',
  `consignee_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收货人姓名',
  `detail` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `sex` tinyint(1) DEFAULT '0' COMMENT '性别,0女,1男',
  `label` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签,0默认,1家,2学校,3公司,4其他',
  `user_id` bigint(20) DEFAULT NULL COMMENT '订单归属用户的主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单详情信息表';

/*Data for the table `ss_order_detail` */

/*Table structure for table `ss_order_operation_record` */

DROP TABLE IF EXISTS `ss_order_operation_record`;

CREATE TABLE `ss_order_operation_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '主订单主键',
  `before_status` tinyint(1) DEFAULT '0' COMMENT '改变前状态',
  `after_status` tinyint(1) DEFAULT '0' COMMENT '改变后状态',
  `note` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '注释或备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `type` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作类型：配合枚举',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单操作记录信息表';

/*Data for the table `ss_order_operation_record` */

/*Table structure for table `ss_payment_record` */

DROP TABLE IF EXISTS `ss_payment_record`;

CREATE TABLE `ss_payment_record` (
  `id` bigint(20) DEFAULT NULL COMMENT '主键ID',
  `pay_type` tinyint(1) DEFAULT NULL COMMENT '存根类型,0支付回调存根,1支付异步通知存根,2退款存根',
  `pay_type_title` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '存根类型标题,0支付回调存根,1支付异步通知存根,2退款存根',
  `total_amount` decimal(12,0) DEFAULT NULL COMMENT '订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位',
  `receipt_amount` decimal(12,0) DEFAULT NULL COMMENT '实收金额。商家在交易中实际收到的款项，单位为人民币（元），精确到小数点后 2 位',
  `invoice_amount` decimal(12,0) DEFAULT NULL COMMENT '开票金额。用户在交易中支付的可开发票的金额，单位为人民币（元），精确到小数点后 2 位',
  `buyer_pay_amount` decimal(12,0) DEFAULT NULL COMMENT '用户在交易中支付的金额，单位为人民币（元），精确到小数点后 2 位',
  `point_amount` decimal(12,0) DEFAULT NULL COMMENT '使用集分宝支付金额，单位为人民币（元），精确到小数点后 2 位',
  `refund_fee` decimal(12,0) DEFAULT NULL COMMENT '总退款金额。退款通知中，返回总退款金额，单位为人民币（元），精确到小数点后 2 位',
  `charset` varchar(48) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字符编码格式',
  `method` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务名,接口名',
  `sign` varchar(1152) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '签名,详情可查看,异步返回结果的验签',
  `auth_app_id` varchar(96) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '授权方的APPID。由于本接口暂不开放第三方应用授权，因此 auth_app_id=app_id',
  `version` varchar(24) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付应用版本',
  `sign_type` varchar(48) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '加密类型,签名类型',
  `trade_no` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付宝交易号，支付宝交易凭证号',
  `app_id` varchar(96) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付宝应用的APPID。支付宝分配给开发者的应用 ID',
  `out_trade_no` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商家订单号。原支付请求的商家订单号',
  `out_biz_no` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商家业务号。商家业务ID，通常是退款通知中返回的退款申请流水号',
  `buyer_id` varchar(48) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '买家支付宝账号 ID。以 2088 开头的纯 16 位数字',
  `seller_id` varchar(96) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '卖家支付宝账号 ID。以 2088 开头的纯 16 位数字',
  `trade_status` varchar(96) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易状态。交易目前所处状态，详情可查看下表 交易状态说明',
  `notify_type` varchar(192) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '通知类型',
  `notify_id` varchar(384) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '通知校验 ID',
  `subject` varchar(768) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单标题/商品标题/交易标题/订单关键字等，是请求时对应参数，会在通知中原样传回',
  `body` varchar(1536) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，会在通知中原样传回',
  `fund_bill_list` varchar(1536) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付金额信息。支付成功的各个渠道金额信息。详情可查看下文 资金明细信息说明',
  `vocher_detail_list` varchar(1536) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '优惠券信息。本交易支付时所使用的所有优惠券信息。详情可查看下表 优惠券信息说明',
  `passback_params` varchar(1536) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '回传参数，公共回传参数，如果请求时传递了该参数，则返回的异步通知会原样传回。本参数必须进行 UrlEncode 之后才可传入',
  `gmt_create` datetime DEFAULT NULL COMMENT '交易创建时间',
  `gmt_payment` datetime DEFAULT NULL COMMENT '交易付款时间',
  `gmt_refund` datetime DEFAULT NULL COMMENT '交易退款时间',
  `gmt_close` datetime DEFAULT NULL COMMENT '交易结束时间',
  `timestamp` datetime DEFAULT NULL COMMENT '支付时间',
  `notify_time` datetime DEFAULT NULL COMMENT '通知的发送时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT NULL COMMENT '删除标志（0代表未删除，1代表已删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='支付记录信息表';

/*Data for the table `ss_payment_record` */

/*Table structure for table `ss_product` */

DROP TABLE IF EXISTS `ss_product`;

CREATE TABLE `ss_product` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `category_id` bigint(20) NOT NULL COMMENT '绑定分类主键',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品名称',
  `description` varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `image_def_url` varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品主图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT '产品价格',
  `sort` tinyint(100) DEFAULT NULL COMMENT '顺序,越大排序最前',
  `status` tinyint(1) DEFAULT NULL COMMENT '产品状态,0未上架,1已上架',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='产品详情信息表';

/*Data for the table `ss_product` */

insert  into `ss_product`(`id`,`category_id`,`name`,`description`,`image_def_url`,`price`,`sort`,`status`,`update_time`,`update_by_id`,`create_time`,`create_by_id`,`is_delete`) values 
(1397849739276890114,1397844263642378242,'辣子鸡','来自鲜嫩美味的小鸡，值得一尝','f966a38e-0780-40be-bb52-5699d13cb3d9.jpg','78.00',0,1,'2021-05-27 09:38:43',1,'2021-05-27 09:38:43',1,0),
(1397850140982161409,1397844263642378242,'毛氏红烧肉','毛氏红烧肉毛氏红烧肉，确定不来一份？','0a3b3288-3446-4420-bbff-f263d0c02d8e.jpg','68.00',0,1,'2021-05-27 09:40:19',1,'2021-05-27 09:40:19',1,0),
(1397850392090947585,1397844263642378242,'组庵鱼翅','组庵鱼翅，看图足以表明好吃程度','740c79ce-af29-41b8-b78d-5f49c96e38c4.jpg','48.00',0,1,'2021-05-27 09:41:19',1,'2021-05-27 09:41:19',1,0),
(1397850851245600769,1397844263642378242,'霸王别姬','还有什么比霸王别姬更美味的呢？','057dd338-e487-4bbc-a74c-0384c44a9ca3.jpg','128.00',0,1,'2021-05-27 09:43:08',1,'2021-05-27 09:43:08',1,0),
(1397851099502260226,1397844263642378242,'全家福','别光吃肉啦，来份全家福吧，让你长寿又美味','a53a4e6a-3b83-4044-87f9-9d49b30a8fdc.jpg','118.00',0,1,'2021-05-27 09:44:08',1,'2021-05-27 09:44:08',1,0),
(1397851370462687234,1397844263642378242,'邵阳猪血丸子','看，美味不？来嘛来嘛，这才是最爱吖','2a50628e-7758-4c51-9fbb-d37c61cdacad.jpg','138.00',0,1,'2021-05-27 09:45:12',1,'2021-05-27 09:45:12',1,0),
(1397851668262465537,1397844263642378242,'口味蛇','爬行界的扛把子，东兴-口味蛇，让你欲罢不能','0f4bd884-dc9c-4cf9-b59e-7d5958fec3dd.jpg','168.00',0,1,'2021-05-27 09:46:23',1,'2021-05-27 09:46:23',1,0),
(1397852391150759938,1397844303408574465,'辣子鸡丁','辣子鸡丁，辣子鸡丁，永远的魂','ef2b73f2-75d1-4d3a-beea-22da0e1421bd.jpg','88.00',0,1,'2021-05-27 09:49:16',1,'2021-05-27 09:49:16',1,0),
(1397853183287013378,1397844303408574465,'麻辣兔头','麻辣兔头的详细制作，麻辣鲜香，色泽红润，回味悠长','2a2e9d66-b41d-4645-87bd-95f2cfeed218.jpg','198.00',0,1,'2021-05-27 09:52:24',1,'2021-05-27 09:52:24',1,0),
(1397853709101740034,1397844303408574465,'蒜泥白肉','多么的有食欲啊','d2f61d70-ac85-4529-9b74-6d9a2255c6d7.jpg','98.00',0,1,'2021-05-27 09:54:30',1,'2021-05-27 09:54:30',1,0),
(1397853890262118402,1397844303408574465,'鱼香肉丝','鱼香肉丝简直就是我们童年回忆的一道经典菜，上学的时候点个鱼香肉丝盖饭坐在宿舍床上看着肥皂剧，绝了！现在完美复刻一下上学的时候感觉','8dcfda14-5712-4d28-82f7-ae905b3c2308.jpg','38.00',0,1,'2021-05-27 09:55:13',1,'2021-05-27 09:55:13',1,0),
(1397854652581064706,1397844303408574465,'麻辣水煮鱼','鱼片是买的切好的鱼片，放几个虾，增加味道','1fdbfbf3-1d86-4b29-a3fc-46345852f2f8.jpg','148.00',0,1,'2021-05-27 09:58:15',1,'2021-05-27 09:58:15',1,0),
(1397854865672679425,1397844303408574465,'鱼香炒鸡蛋','鱼香菜也是川味的特色。里面没有鱼却鱼香味','0f252364-a561-4e8d-8065-9a6797a6b1d3.jpg','20.00',0,1,'2021-05-27 09:59:06',1,'2021-05-27 09:59:06',1,0),
(1397860242057375745,1397844391040167938,'脆皮烧鹅','“广东烤鸭美而香，却胜烧鹅说古冈（今新会），燕瘦环肥各佳妙，君休偏重便宜坊”，可见烧鹅与烧鸭在粤菜之中已早负盛名。作为广州最普遍和最受欢迎的烧烤肉食，以它的“色泽金红，皮脆肉嫩，味香可口”的特色，在省城各大街小巷的烧卤店随处可见。','e476f679-5c15-436b-87fa-8c4e9644bf33.jpeg','128.00',0,1,'2021-05-27 10:20:27',1,'2021-05-27 10:20:27',1,0),
(1397860578738352129,1397844391040167938,'白切鸡','白切鸡是一道色香味俱全的特色传统名肴，又叫白斩鸡，是粤菜系鸡肴中的一种，始于清代的民间。白切鸡通常选用细骨农家鸡与沙姜、蒜茸等食材，慢火煮浸白切鸡皮爽肉滑，清淡鲜美。著名的泮溪酒家白切鸡，曾获商业部优质产品金鼎奖。湛江白切鸡更是驰名粤港澳。粤菜厨坛中，鸡的菜式有200余款之多，而最为人常食不厌的正是白切鸡，深受食家青睐。','9ec6fc2d-50d2-422e-b954-de87dcd04198.jpeg','66.00',0,1,'2021-05-27 10:21:48',1,'2021-05-27 10:21:48',1,0),
(1397860792492666881,1397844391040167938,'烤乳猪','广式烧乳猪主料是小乳猪，辅料是蒜，调料是五香粉、芝麻酱、八角粉等，本菜品主要通过将食材放入炭火中烧烤而成。烤乳猪是广州最著名的特色菜，并且是“满汉全席”中的主打菜肴之一。烤乳猪也是许多年来广东人祭祖的祭品之一，是家家都少不了的应节之物，用乳猪祭完先人后，亲戚们再聚餐食用。','2e96a7e3-affb-438e-b7c3-e1430df425c9.jpeg','388.00',0,1,'2021-05-27 10:22:39',1,'2021-05-27 10:22:39',1,0),
(1397860963880316929,1397844391040167938,'脆皮乳鸽','“脆皮乳鸽”是广东菜中的一道传统名菜，属于粤菜系，具有皮脆肉嫩、色泽红亮、鲜香味美的特点，常吃可使身体强健，清肺顺气。随着菜品制作工艺的不断发展，逐渐形成了熟炸法、生炸法和烤制法三种制作方法。无论那种制作方法，都是在鸽子经过一系列的加工，挂脆皮水后再加工而成，正宗的“脆皮乳鸽皮脆肉嫩、色泽红亮、鲜香味美、香气馥郁。这三种方法的制作过程都不算复杂，但想达到理想的效果并不容易。','3fabb83a-1c09-4fd9-892b-4ef7457daafa.jpeg','108.00',0,1,'2021-05-27 10:23:19',1,'2021-05-27 10:23:19',1,0),
(1397861683434139649,1397844391040167938,'清蒸河鲜海鲜','新鲜的海鲜，清蒸是最好的处理方式。鲜，体会为什么叫海鲜。清蒸是广州最经典的烹饪手法，过去岭南地区由于峻山大岭阻隔，交通不便，经济发展起步慢，自家打的鱼放在锅里煮了就吃，没有太多的讲究，但却发现这清淡的煮法能使鱼的鲜甜跃然舌尖。','1405081e-f545-42e1-86a2-f7559ae2e276.jpeg','388.00',0,1,'2021-05-27 10:26:11',1,'2021-05-27 10:26:11',1,0),
(1397862198033297410,1397844391040167938,'老火靓汤','老火靓汤又称广府汤，是广府人传承数千年的食补养生秘方，慢火煲煮的中华老火靓汤，火候足，时间长，既取药补之效，又取入口之甘甜。 广府老火汤种类繁多，可以用各种汤料和烹调方法，烹制出各种不同口味、不同功效的汤来。','583df4b7-a159-4cfc-9543-4f666120b25f.jpeg','49.00',0,1,'2021-05-27 10:28:14',1,'2021-05-27 10:28:14',1,0),
(1397862477831122945,1397844391040167938,'上汤焗龙虾','上汤焗龙虾是一道色香味俱全的传统名菜，属于粤菜系。此菜以龙虾为主料，配以高汤制成的一道海鲜美食。本品肉质洁白细嫩，味道鲜美，蛋白质含量高，脂肪含量低，营养丰富。是色香味俱全的传统名菜。','5b8d2da3-3744-4bb3-acdc-329056b8259d.jpeg','108.00',0,1,'2021-05-27 10:29:20',1,'2021-05-27 10:29:20',1,0),
(1413342036832100354,1413341197421846529,'北冰洋','你喝的只是汽水，我喝的是北冰洋','c99e0aab-3cb7-4eaa-80fd-f47d4ffea694.png','5.00',0,1,'2021-07-09 15:12:18',1,'2021-07-09 11:39:35',1,0),
(1413384757047271425,1413341197421846529,'王老吉','怕上火就喝加多宝','2f834270-fc30-4f65-9a3c-c80da2d0b803.png','5.00',0,1,'2022-06-19 23:45:38',1,'2021-07-09 14:29:20',1,0),
(1413385247889891330,1413384954989060097,'米饭','香甜可口的米饭，得到碳水教父的认可','ee04a05a-1230-46b6-8ad5-1a95b140fff3.png','2.00',0,1,'2022-06-19 23:43:42',1,'2021-07-09 14:31:17',1,0);

/*Table structure for table `ss_product_attributes` */

DROP TABLE IF EXISTS `ss_product_attributes`;

CREATE TABLE `ss_product_attributes` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `product_id` bigint(20) NOT NULL COMMENT '用户积分表主键ID',
  `attributes_id` bigint(20) NOT NULL COMMENT '用户积分历史记录表主键ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='产品和属性关系中间表';

/*Data for the table `ss_product_attributes` */

insert  into `ss_product_attributes`(`id`,`product_id`,`attributes_id`,`create_time`,`create_by_id`) values 
(1,1397854652581064706,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(2,1397849739276890114,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(3,1397861898438356993,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(5,1398090825329061889,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(6,1397850140982161409,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(7,1398091546480914433,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(8,1397853709101740034,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(9,1397849936404983809,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(10,1397851099502260226,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(11,1397862198033297410,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(12,1397855906468245506,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(13,1398090003228700673,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(14,1398090264517062657,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(15,1397861683434139649,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(16,1397850630700707841,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(17,1397851370462687234,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(18,1397852391150759938,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(19,1397850851245600769,1397854652623007745,'2022-06-25 19:04:53',1539977016415780865),
(20,1397861370010578945,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(21,1397859056684150785,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(22,1399305325713600514,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(23,1398091296131297281,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(24,1398092095142014978,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(25,1397850392090947585,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(26,1397850392090947585,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(27,1398089782285348866,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(28,1398091729788776450,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(29,1397849936404983809,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(30,1397850851245600769,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(31,1397849417854791681,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(32,1397860963880316929,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(33,1398091007017922561,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(34,1397850140982161409,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(35,1398091889449152513,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(36,1397853890262118402,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(37,1397855742273826817,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(38,1397853183287013378,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(39,1397856190540066818,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(40,1397860242057375745,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(41,1398094391456346113,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(42,1397859487476920321,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(43,1398094018893099009,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(44,1397859277812051969,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(45,1397854865672679425,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(46,1398092283847946241,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(47,1397849739276890114,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(48,1397853423461249026,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(49,1397854652581064706,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(50,1398090455324340225,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(51,1397851370462687234,1397859056709316609,'2022-06-25 19:04:53',1539977016415780865),
(52,1397851099502260226,1397862477835317250,'2022-06-25 19:04:53',1539977016415780865),
(53,1397862477831122945,1397862477835317250,'2022-06-25 19:04:53',1539977016415780865),
(54,1397852391150759938,1397862477835317250,'2022-06-25 19:04:53',1539977016415780865),
(55,1413384757047271425,1413389540592263169,'2022-06-25 19:04:53',1539977016415780865),
(56,1413342036832100354,1413389540592263169,'2022-06-25 19:04:53',1539977016415780865),
(57,1397850630700707841,1397862477835317250,'2022-06-25 19:04:53',1539977016415780865),
(58,1397859757036449794,1397859757061615618,'2022-06-25 19:04:53',1539977016415780865),
(59,1397861135733534722,1397859757061615618,'2022-06-25 19:04:53',1539977016415780865),
(60,1398089545676271617,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865),
(61,1397854133603053569,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865),
(62,1397851370462687234,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865),
(63,1397851668262465537,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865),
(64,1398090685419663362,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865),
(65,1413385247889891330,1397854133632413697,'2022-06-25 19:04:53',1539977016415780865);

/*Table structure for table `ss_refund_operation_record` */

DROP TABLE IF EXISTS `ss_refund_operation_record`;

CREATE TABLE `ss_refund_operation_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `refund_id` bigint(20) DEFAULT NULL COMMENT '申请退款表主键',
  `before_status` tinyint(1) DEFAULT '0' COMMENT '改变前状态',
  `after_status` tinyint(1) DEFAULT '0' COMMENT '改变后状态',
  `type` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '枚举值',
  `note` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '注释或备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='申请退款操作记录信息表';

/*Data for the table `ss_refund_operation_record` */

/*Table structure for table `ss_refund_record` */

DROP TABLE IF EXISTS `ss_refund_record`;

CREATE TABLE `ss_refund_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `out_trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `buyer_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '买家名称',
  `buyer_phone_number` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '买家联系电话',
  `buyer_reason` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '买家申请内容',
  `status` tinyint(1) DEFAULT '0' COMMENT '退款申请状态,0买家申请提交,1卖家处理,2系统退款,3完成,4系统退款异常,5卖拒绝退款,6买家取消退款',
  `handle_reply` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '卖家回复内容',
  `price` decimal(10,2) DEFAULT NULL COMMENT '本次退款额，精确到小数点后 2 位',
  `type` tinyint(1) DEFAULT NULL COMMENT '退款类型:0系统自动退款, 1用户发起退款, 2商家发起退款',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='申请退款信息表';

/*Data for the table `ss_refund_record` */

/*Table structure for table `ss_trade_refund_record` */

DROP TABLE IF EXISTS `ss_trade_refund_record`;

CREATE TABLE `ss_trade_refund_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额。 需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数',
  `refund_fee` decimal(10,2) DEFAULT NULL COMMENT '退款总金额。指该笔交易累计已经退款成功的金额，单位为元，支持两位小数',
  `send_back_fee` decimal(10,2) DEFAULT NULL COMMENT '本次商户实际退回金额。说明：如需获取该值，需在入参query_options中传入 refund_detail_item_list，单位为元，支持两位小数',
  `refund_reason` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款原因说明。商家自定义，将在会在商户和用户的pc退款账单详情中展示',
  `out_request_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款请求号。标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传',
  `code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '网关返回码',
  `msg` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '网关返回码描述',
  `sub_code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务返回码',
  `trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付宝交易号，支付宝交易凭证号',
  `out_trade_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商户订单号',
  `buyer_logon_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户的登录id',
  `fund_change` varchar(8) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本次退款是否发生了资金变化',
  `refund_detail_item_list` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退款使用的资金渠道。只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。',
  `store_name` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易在支付时候的门店名称',
  `buyer_user_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '买家在支付宝的用户id',
  `sign` varchar(384) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '签名,详情可查看,异步返回结果的验签',
  `gmt_refund_pay` datetime DEFAULT NULL COMMENT '退款时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='退款存根信息表';

/*Data for the table `ss_trade_refund_record` */

/*Table structure for table `ss_user` */

DROP TABLE IF EXISTS `ss_user`;

CREATE TABLE `ss_user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `avatar` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `nickname` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户昵称',
  `phone_number` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `description` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `email` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女,1男',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 0:正常,1:禁用',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息详情表';

/*Data for the table `ss_user` */

insert  into `ss_user`(`id`,`avatar`,`nickname`,`phone_number`,`description`,`email`,`sex`,`status`,`update_time`,`update_by_id`,`create_time`,`create_by_id`,`is_delete`) values 
(1540278005366652929,'http://10.10.10.55:9000/reggie/static/user/avatar/7.jpeg','19112345678手机用户','19112345678','吃饱饱','183101655@qq.com',0,0,'2022-06-24 18:17:48',1540278005366652929,'2022-06-24 18:17:48',1540278005366652929,0);

/*Table structure for table `ss_user_address_book` */

DROP TABLE IF EXISTS `ss_user_address_book`;

CREATE TABLE `ss_user_address_book` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '绑定用户主键ID',
  `consignee_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收货人姓名',
  `phone_number` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女,1男',
  `detail` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `label` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) DEFAULT NULL COMMENT '是否默认,0否,1是',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人的ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人的ID',
  `is_delete` tinyint(1) DEFAULT NULL COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户地址簿信息表';

/*Data for the table `ss_user_address_book` */

insert  into `ss_user_address_book`(`id`,`user_id`,`consignee_name`,`phone_number`,`sex`,`detail`,`label`,`is_default`,`update_time`,`update_by_id`,`create_time`,`create_by_id`,`is_delete`) values 
(1554026857697554434,1540278005366652929,'热心市民郭先生','19112345678',1,'海淀区五道口职业技术学校','默认',1,'2022-08-05 21:43:56',1540278005366652929,'2022-08-01 16:50:50',1540278005366652929,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
