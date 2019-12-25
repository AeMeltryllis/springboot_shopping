-- 创建数据库
DROP DATABASE IF EXISTS graduation_project;
CREATE DATABASE graduation_project DEFAULT CHARACTER SET utf8;
-- 创建用户表
CREATE TABLE user_info (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  create_time datetime ,
  update_time datetime ,
  pass_word varchar(255) DEFAULT NULL,
  salt varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建商品分类
CREATE TABLE category_info (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  name varchar(255) not null,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 创建属性表
CREATE TABLE property (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  category_id bigint(11) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建产品表
CREATE TABLE product_info (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  name varchar(255) DEFAULT NULL,
  sub_title varchar(255) DEFAULT NULL,
  original_price float DEFAULT NULL COMMENT '原始价格',
  promote_price float DEFAULT NULL COMMENT '优惠价格',
  stock int(11) DEFAULT NULL COMMENT '库存',
  category_id bigint(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 属性的值表
CREATE TABLE property_value (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  product_id bigint(11) DEFAULT NULL,
  property_id bigint(11) DEFAULT NULL,
  value varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 产品图片表
CREATE TABLE product_image (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  product_id bigint(11) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 评价表
CREATE TABLE review (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  content varchar(4000) DEFAULT NULL,
  user_id bigint(11)  DEFAULT NULL,
  product_id bigint(11)  DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 订单信息表
CREATE TABLE order_info (
  id bigint(11) NOT NULL AUTO_INCREMENT ,
  create_time datetime ,
  update_time datetime ,
  order_code varchar(255) DEFAULT NULL COMMENT '订单编号',
  address varchar(255) DEFAULT NULL COMMENT '收货地址',
  post_code varchar(255) DEFAULT NULL COMMENT '邮政编码',
  receiver_name varchar(255) DEFAULT NULL,
  mobile_number varchar(255) DEFAULT NULL,
  user_remark varchar(255) DEFAULT NULL COMMENT '用户备注' ,
  create_date datetime DEFAULT NULL,
  pay_data datetime DEFAULT NULL,
  delivery_data datetime DEFAULT NULL COMMENT '发货日期',
  confirm_delivery_date datetime DEFAULT NULL COMMENT '确认收货日期',
  user_id bigint(11)  DEFAULT NULL,
  order_status varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 订单项表
CREATE TABLE order_item (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  create_time datetime ,
  update_time datetime ,
  product_id bigint(11) DEFAULT NULL,
  order_info_id bigint(11) DEFAULT NULL,
  user_id bigint(11) DEFAULT NULL,
  product_number int(11) DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- 2019/12/25 为category数据表增加逻辑删除

alter table category_info ADD  COLUMN is_deleted bit(1) DEFAULT NULL COMMENT '逻辑删除标记';





