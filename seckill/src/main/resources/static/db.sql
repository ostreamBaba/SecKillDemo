CREATE TABLE `sk_user`(
  `id` BIGINT(20) NOT NULL COMMENT '用户id 手机号码',
  `nickname` VARCHAR(255) NOT NULL,
  `password` VARCHAR(36) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt))+salt',
  `salt` VARCHAR(10) DEFAULT NULL,
  `head` VARCHAR(128) DEFAULT NULL COMMENT '头像 云存储的id',
  `register_data` DATETIME DEFAULT NULL COMMENT '注册时间',
  `last_login_date` DATETIME DEFAULT NULL COMMENT '上次登录时间',
  `login_count` INT(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#秒杀数据库设计
CREATE TABLE `goods`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
  `goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
  `goods_detail` LONGTEXT COMMENT '商品详情介绍',
  `goods_price` DECIMAL(10, 2) DEFAULT '0.00' COMMENT '商品单价',
  `goods_stock` INT(11) DEFAULT '0' COMMENT '商品库存, -1表示没有限制',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;