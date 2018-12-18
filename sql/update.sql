/*
author:leijinjun
Date: 2018-09-16 19:52:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dy_noble
-- ----------------------------
DROP TABLE IF EXISTS `dy_noble`;
CREATE TABLE `dy_noble` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `num` int(11) DEFAULT '0',
  `rid` int(11) DEFAULT NULL,
  `nl` varchar(5000) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for dy_frank
-- ----------------------------
DROP TABLE IF EXISTS `dy_frank`;
CREATE TABLE `dy_frank` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rid` int(11) NOT NULL,
  `fc` int(11) NOT NULL DEFAULT '0',
  `bnn` varchar(20) DEFAULT '',
  `top10` varchar(2048) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for dy_room_conntect
-- ----------------------------
DROP TABLE IF EXISTS `dy_room_connect`;
CREATE TABLE `dy_room_connect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `owner_name` varchar(128) DEFAULT NULL COMMENT '房间主播昵称',
  `connect` tinyint(1) DEFAULT '1' COMMENT '1:开启连接，0:不启用',
  `create_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('1', '533813', 'TD丶正直博', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('2', '988', '纯白', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('3', '74751', '超级小桀', '0', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('4', '606118', '芜湖大司马丶', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('5', '78561', '妃凌雪', '0', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('6', '229346', '高冷男神钱小佳', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('7', '520', '阿冷aleng丶', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('8', '156277', '女流66', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('9', '290935', '不2不叫周淑怡', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('10', '430489', '长沙乡村敢死队', '1', '2018-11-18 18:20:30', '2018-11-18 18:20:30');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('11', '99999', '旭旭宝宝', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('12', '688', '指法芬芳张大仙', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('13', '485503', '彡彡九户外', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('14', '24422', 'pigff', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('15', '20360', 'Pc冷冷', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('16', '554559', '峰峰三号333', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('17', '5146671', '苏梓夏夏', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('18', '5706218', '奔跑的姜允儿', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('19', '105025', '105025', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('20', '2947432', '呆妹儿小霸王', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('21', '241431', '小苏菲', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('22', '25233', '小深深儿', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');
INSERT INTO `dy_room_connect` (`id`, `room_id`, `owner_name`, `connect`, `create_at`, `update_at`) VALUES ('23', '3559749', '苏恩Olivia', '1', '2018-11-18 17:30:55', '2018-11-18 17:30:55');

CREATE TABLE `u_user` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`username`  varchar(10) NOT NULL COMMENT '用户名' ,
`phone`  varchar(20) NULL ,
`email`  varchar(31) NULL ,
`icon`  varchar(255) NULL ,
`password`  varchar(31) NOT NULL COMMENT '密码' ,
`pass_salt`  varchar(32) NULL COMMENT '密码盐' ,
`last_login_ip`  int(10) NULL COMMENT '上次登录IP' ,
`last_login_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP ,
`cur_login_ip`  int(10) NULL COMMENT '本次登录IP' ,
`cur_login_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '本次登录时间' ,
`status`  tinyint(1) NOT NULL COMMENT '状态,1=正常，0=封禁' ,
`regist_ip`  int(10) NULL COMMENT '注册IP' ,
`create_at`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_at`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建人' ,
`create_by`  int(11) NULL COMMENT '记录创建人' ,
`update_by`  int(11) NULL COMMENT '记录更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `username_index` (`username`) USING BTREE
);