package com.example.jwt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Data
public class SysUser {
    /**
     * 用户Id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 姓名
     */
    private String fullName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 是否阻止登录：0否，其他是
     */
    private Integer loginFlag;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
    /**
     * 删除标记：0未删，其他删除
     */
    private Integer delFlag;
    /**
     * 角色Ids，用","隔开
     */
    private String roleIds;
    /**
     * 角色名称集合
     */
    private List<String> roles;
    /**
     * 角色描述，用" "隔开
     */
    private String roleDesc;
    /**
     * 可访问菜单集合
     */
    private List<SysMenu> menus;
    /**
     * 指令权限集合
     */
    List<String> permissions;

    private static final long serialVersionUID = 1L;
}
