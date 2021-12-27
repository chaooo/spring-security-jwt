-- 系统用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(255) UNIQUE NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `avatar` VARCHAR(255) DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif' COMMENT '头像',
  `fullName` VARCHAR(100) DEFAULT NULL COMMENT '姓名',
  `phone` INT(20) DEFAULT NULL COMMENT '电话',
  `login_flag` CHAR(1) NOT NULL DEFAULT '0' COMMENT '是否阻止登录：0否，其他是',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='系统用户表';
-- 系统角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) UNIQUE  NOT NULL COMMENT '角色名称',
  `role_desc` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='系统角色表';
-- 系统菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `title` VARCHAR(100) NOT NULL COMMENT '菜单名称',
  `name` VARCHAR(100) UNIQUE NOT NULL COMMENT '路由名称(前端匹配路由用)',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `parent_id` BIGINT NOT NULL DEFAULT '0' COMMENT '父级菜单Id',
  `hidden` CHAR(1) NOT NULL DEFAULT '0' COMMENT '隐藏状态：0显示，1隐藏',
  `status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态：0启用，1停用',
  `sort` INT(10) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='系统菜单表';
-- 用户&角色 关联表
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_id` VARCHAR(50) DEFAULT NULL COMMENT '角色ID',
  `user_id` VARCHAR(255) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='系统用户角色关联表';
-- 菜单&角色 关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_id` VARCHAR(50) DEFAULT NULL COMMENT '角色ID',
  `menu_id` VARCHAR(255) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='系统用户角色关联表';
-- 初始数据
INSERT INTO `sys_user`(`id`,`username`,`password`,`create_time`) VALUES (1,'admin','$2a$10$QmDp1600wURZ.Dn2utkfXO4UTM3gdV42qVjMa81o3GMyW.IdfeEWm',NOW());
INSERT INTO `sys_role`(`id`,`role_name`,`role_desc`,`create_time`) VALUES (1,'admin','管理员',NOW());
INSERT INTO `sys_menu` (`id`, `title`, `name`, `icon`, `parent_id`, `hidden`, `create_time`)
VALUES  ('1','系统设置','SysSetting','el-icon-s-tools','0','0',NOW()),
        ('2','菜单管理','SysMenus','el-icon-menu','1','0',NOW()),
        ('3','角色管理','SysRoles','peoples','1','0',NOW()),
        ('4','用户管理','SysUsers','user','1','0',NOW()),
        ('5','系统图标','SysIcons','el-icon-picture','1','0',NOW()),
        ('6','菜单列表','SysMenuList','','2','1',NOW()),
        ('7','菜单编辑','SysMenuEdit','','2','1',NOW()),
        ('8','角色列表','SysRoleList','','3','1',NOW()),
        ('9','角色编辑','SysRoleEdit','','3','1',NOW()),
        ('10','用户列表','SysUserList','','4','1',NOW()),
        ('11','用户编辑','SysUserEdit','','4','1',NOW());
INSERT INTO `sys_role_user`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 1),(1, 2),(1, 3),(1, 4),(1, 5),(1, 6),(1, 7),(1, 8),(1, 9),(1, 10),(1, 11);
