package com.example.jwt.service;

import com.example.jwt.entity.PaginationVo;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysMenu;
import com.example.jwt.entity.SysMenuTree;

import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/2/8
 */
public interface SysMenuService {
    /**
     * 获取菜单列表
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    ResponseJson<PaginationVo<SysMenu>> getMenuList(Integer parentId, Integer status, int pageNum, int pageSize);
    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    ResponseJson<List<SysMenuTree>> getMenuTree();
    /**
     * 根据ID获取菜单
     */
    ResponseJson<SysMenu> getMenu(Integer id);
    /**
     * 根据ID更新菜单
     */
    ResponseJson<Void> updateMenu(Integer id, SysMenu menu);
    /**
     * 根据ID(可选择的)更新菜单字段
     */
    ResponseJson<Void> updateMenuSelective(Integer id, SysMenu menu);
    /**
     * 添加菜单
     */
    ResponseJson<Void> addMenu(SysMenu menu);
    /**
     * 根据ID删除菜单
     */
    ResponseJson<Void> deleteMenu(Integer id);
}
