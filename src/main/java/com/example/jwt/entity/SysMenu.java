package com.example.jwt.entity;

import lombok.Data;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/10
 */
@Data
public class SysMenu {
    /**
     * Id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单路径
     */
    private String menuPath;
    /**
     * 菜单类型(1:一级菜单，2:子菜单，3:按钮)
     */
    private char menuType;
    /**
     * 父级菜单Id
     */
    private Long parentId;

    private static final long serialVersionUID = 1L;
}
