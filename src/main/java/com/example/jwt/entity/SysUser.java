package com.example.jwt.entity;

import lombok.Data;

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
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色Ids，用","隔开
     */
    private String roleIds;
    /**
     * 角色名称集合
     */
    private List<String> roles;

    private static final long serialVersionUID = 1L;
}
