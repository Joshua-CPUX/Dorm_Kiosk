-- 校园小卖部数据库脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS dorm_kiosk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dorm_kiosk;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
  `nickname` VARCHAR(64) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `phone` VARCHAR(20) COMMENT '手机号',
  `balance` DECIMAL(10,2) DEFAULT 0 COMMENT '余额',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 管理员表
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(64) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 商品分类表
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(255) COMMENT '分类图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `name` VARCHAR(128) NOT NULL COMMENT '商品名称',
  `subtitle` VARCHAR(256) COMMENT '副标题',
  `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2) COMMENT '原价',
  `stock` INT DEFAULT 0 COMMENT '库存',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `image` VARCHAR(512) COMMENT '主图',
  `images` TEXT COMMENT '图片列表JSON',
  `description` TEXT COMMENT '商品描述',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
DROP TABLE IF EXISTS `oms_cart`;
CREATE TABLE `oms_cart` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 收货地址表
DROP TABLE IF EXISTS `ums_address`;
CREATE TABLE `ums_address` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `consignee` VARCHAR(64) NOT NULL COMMENT '收货人',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `province` VARCHAR(32) COMMENT '省份',
  `city` VARCHAR(32) COMMENT '城市',
  `district` VARCHAR(32) COMMENT '区县',
  `detail` VARCHAR(256) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 订单表
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总额',
  `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `freight` DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
  `pay_type` TINYINT COMMENT '支付方式 1-微信支付',
  `pay_time` DATETIME COMMENT '支付时间',
  `pay_status` TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-已支付',
  `order_type` TINYINT NOT NULL COMMENT '订单类型 1-自取 2-配送',
  `status` TINYINT DEFAULT 1 COMMENT '订单状态 1-待支付 2-已支付 3-配送中 4-已完成 5-已取消',
  `address_id` BIGINT COMMENT '收货地址ID',
  `remark` VARCHAR(256) COMMENT '备注',
  `cancel_time` DATETIME COMMENT '取消时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品明细表
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(128) NOT NULL COMMENT '商品名称',
  `product_image` VARCHAR(512) COMMENT '商品图片',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
  `quantity` INT NOT NULL COMMENT '购买数量',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品明细表';

-- 管理员表初始化数据
INSERT INTO `sys_admin` (`username`, `password`, `nickname`) VALUES
('admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', '管理员');

-- 分类初始化数据
INSERT INTO `pms_category` (`name`, `icon`, `sort`) VALUES
('零食', '/icons/snacks.png', 1),
('饮料', '/icons/drinks.png', 2),
('方便食品', '/icons/instant.png', 3),
('日用品', '/icons/daily.png', 4),
('文具', '/icons/stationery.png', 5);

-- 商品初始化数据
INSERT INTO `pms_product` (`category_id`, `name`, `subtitle`, `price`, `original_price`, `stock`, `sales`, `image`, `description`) VALUES
-- 零食类
(1, '奥利奥饼干', '经典原味夹心饼干', 9.90, 12.90, 100, 50, 'https://img.yzcdn.cn/vant/cat.jpeg', '美味的奥利奥夹心饼干，甜而不腻'),
(1, '薯片组合', '多种口味可选', 15.80, 19.90, 80, 35, 'https://img.yzcdn.cn/vant/cat.jpeg', '酥脆薯片，追剧必备零食'),
(1, '巧克力派', '12枚装', 24.90, 29.90, 60, 28, 'https://img.yzcdn.cn/vant/cat.jpeg', '浓郁巧克力味的小蛋糕'),
-- 饮料类
(2, '农夫山泉', '550ml装', 2.00, 2.50, 200, 120, 'https://img.yzcdn.cn/vant/cat.jpeg', '天然矿泉水'),
(2, '可口可乐', '330ml罐装', 3.50, 4.00, 150, 80, 'https://img.yzcdn.cn/vant/cat.jpeg', '经典碳酸饮料'),
(2, '冰红茶', '500ml瓶装', 4.50, 5.00, 100, 45, 'https://img.yzcdn.cn/vant/cat.jpeg', '柠檬味茶饮料'),
(2, '纯牛奶', '250ml盒装', 5.00, 6.00, 80, 30, 'https://img.yzcdn.cn/vant/cat.jpeg', '优质纯牛奶，营养健康'),
-- 方便食品类
(3, '康师傅方便面', '红烧牛肉味', 4.50, 5.00, 100, 60, 'https://img.yzcdn.cn/vant/cat.jpeg', '经典口味方便面'),
(3, '自热米饭', '宫保鸡丁味', 15.00, 18.00, 50, 20, 'https://img.yzcdn.cn/vant/cat.jpeg', '即食自热米饭，方便快捷'),
(3, '八宝粥', '易拉罐装', 4.00, 5.00, 70, 25, 'https://img.yzcdn.cn/vant/cat.jpeg', '营养八宝粥，早餐好选择'),
-- 日用品类
(4, '抽纸', '3包装', 9.90, 12.90, 100, 40, 'https://img.yzcdn.cn/vant/cat.jpeg', '柔软抽纸，家庭必备'),
(4, '洗手液', '500ml瓶装', 15.00, 18.00, 60, 15, 'https://img.yzcdn.cn/vant/cat.jpeg', '抑菌洗手液'),
(4, '牙刷', '软毛款', 8.90, 12.00, 80, 22, 'https://img.yzcdn.cn/vant/cat.jpeg', '柔软刷毛，保护牙龈'),
-- 文具类
(5, '中性笔', '黑色 12支装', 12.00, 15.00, 100, 55, 'https://img.yzcdn.cn/vant/cat.jpeg', '顺滑书写中性笔'),
(5, '笔记本', 'A5 100页', 8.00, 10.00, 80, 35, 'https://img.yzcdn.cn/vant/cat.jpeg', '优质纸张，书写流畅'),
(5, '铅笔盒', '多功能款', 18.00, 22.00, 50, 12, 'https://img.yzcdn.cn/vant/cat.jpeg', '多层铅笔盒，收纳方便');
