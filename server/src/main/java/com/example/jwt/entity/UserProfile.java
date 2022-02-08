package com.caimei365.manager.entity.sys;

import lombok.Data;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/1/4
 */
@Data
public class UserProfile {
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
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
     * 密码
     */
    private String password;
    /**
     * 密码
     */
    private String oldPassword;
    /**
     * 密码
     */
    private String confirmPassword;

    private static final long serialVersionUID = 1L;
}
