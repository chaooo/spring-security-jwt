/*
SQLyog Professional v12.09 (64 bit)
MySQL - 8.0.21 : Database - security
*********************************************************************
*/

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';
-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `menu_path` varchar(255) DEFAULT NULL COMMENT '菜单路径',
  `menu_type` char DEFAULT NULL COMMENT '菜单类型(1:一级菜单，2:子菜单，3:按钮)',
  `menu_parent_id` bigint DEFAULT NULL COMMENT '父级菜单Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';
-- 用户&角色 关联表
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `role_id` varchar(50) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';
-- 菜单&角色 关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` varchar(50) DEFAULT NULL COMMENT '角色ID',
  `menu_id` varchar(255) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';
-- 初始数据，管理员拥有所有菜单权限，普通用户拥有查看权限
INSERT INTO `sys_role`(`id`, `role_name`, `role_desc`) VALUES (1, 'admin', '管理员'),(2, 'user', '普通用户');
INSERT INTO `sys_menu`(`id`, `menu_name`,`menu_path`,`menu_type`,`menu_parent_id`)
    VALUES  (1, '用户管理', '/user', 1, null),
            (2, '用户列表', '/user/list', 2, 1),
            (3, '新增用户', '/user/add', 2, 1),
            (4, '修改用户', '/user/update', 2, 1),
            (5, '删除用户', '/user/delete', 3, 1);
INSERT INTO `sys_role_user`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`)
    VALUES  (1, 1),(1, 2),(1, 3),(1, 4),(1, 5),
            (2, 1),(2, 2);


