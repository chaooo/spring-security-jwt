package com.caimei365.manager.entity.sys;

import lombok.Data;

import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/2/7
 */
@Data
public class SysMenuTree {
    /**
     * Id
     */
    private String id;
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
    private String parentId;
    /**
     * 子菜单
     */
    private List<SysMenuTree> subMenus;

    private static final long serialVersionUID = 1L;
}
