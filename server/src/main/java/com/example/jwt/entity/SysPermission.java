package com.example.jwt.entity;

import lombok.Data;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/2/7
 */
@Data
public class SysPermission {
    /**
     * Id
     */
    private Integer id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父级菜单Id
     */
    private Integer menuId;

    private static final long serialVersionUID = 1L;
}
