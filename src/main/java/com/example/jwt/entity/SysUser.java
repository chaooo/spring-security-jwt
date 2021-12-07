package com.example.jwt.entity;

import lombok.Data;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
}
