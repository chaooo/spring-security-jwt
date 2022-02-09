-- 系统用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif' COMMENT '头像',
  `fullName` varchar(100) NOT NULL COMMENT '姓名',
  `phone` varchar(20) NOT NULL COMMENT '电话',
  `login_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否阻止登录：0否，其他是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';

-- 系统角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色表';

-- 系统菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `title` varchar(100) NOT NULL COMMENT '菜单名称',
  `name` varchar(100) NOT NULL COMMENT '路由名称(前端匹配路由用)',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父级菜单Id',
  `hidden` char(1) NOT NULL DEFAULT '0' COMMENT '隐藏状态：0显示，1隐藏',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态：0启用，1停用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0未删，其他删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- 系统权限表
DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `name` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `title` varchar(100) NOT NULL COMMENT '权限名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统权限表';

 -- 权限&角色 关联表
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `permission_id` bigint DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='系统角色权限关联表';

-- 用户&角色 关联表
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='系统用户角色关联表';

-- 菜单&角色 关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='系统角色菜单关联表';

-- 初始数据
INSERT  INTO `sys_user`(`id`,`username`,`password`,`create_time`) VALUES (1,'sysadmin','$2a$10$Ml/uEJ5BnUSdKspYM4vkneUHM0piXyAVU0aueWya/v7FBauz6XWE6',NOW());
INSERT  INTO `sys_role`(`id`,`role_name`,`role_desc`,`create_time`) VALUES (1,'admin','管理员',NOW());
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
        ('11','用户编辑','SysUserEdit','','4','1',NOW()),
        ('12','其他菜单','Other','bug',0,'0',NOW());
INSERT  INTO `sys_permission`(`id`,`menu_id`,`name`,`title`) VALUES (1,6,'sys:menu:edit','编辑'),(2,6,'sys:menu:del','删除'),(3,8,'sys:role:edit','编辑'),(4,8,'sys:role:del','删除'),(5,10,'sys:user:edit','编辑'),(6,10,'sys:user:del','删除');
INSERT INTO `sys_role_user`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 1),(1, 2),(1, 3),(1, 4),(1, 5),(1, 6),(1, 7),(1, 8),(1, 9),(1, 10),(1, 11),(1, 12);
INSERT  INTO `sys_role_permission`(`id`,`role_id`,`permission_id`) VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6);

