package com.caimei365.manager.controller.sys;

import com.caimei365.manager.entity.PaginationVo;
import com.caimei365.manager.entity.ResponseJson;
import com.caimei365.manager.entity.sys.SysMenu;
import com.caimei365.manager.entity.sys.SysMenuTree;
import com.caimei365.manager.service.sys.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/1/6
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuApi {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取菜单列表
     *
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/list")
    public ResponseJson<PaginationVo<SysMenu>> menuList(Integer status,
                                                        @RequestParam(value = "parentId", defaultValue = "0") Integer parentId,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysMenuService.getMenuList(parentId, status, pageNum, pageSize);
    }

    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    @GetMapping("/tree")
    public ResponseJson<List<SysMenuTree>> menuTree() {
        return sysMenuService.getMenuTree();
    }

    /**
     * 根据ID获取菜单
     */
    @GetMapping("/{id}")
    public ResponseJson<SysMenu> getMenu(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysMenuService.getMenu(id);
    }

    /**
     * 根据ID更新菜单
     */
    @PostMapping("/update/{id}")
    public ResponseJson<Void> updateMenu(@PathVariable("id") Integer id, @RequestBody SysMenu menu) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysMenuService.updateMenu(id, menu);
    }

    /**
     * 根据ID(可选择的)更新菜单字段
     */
    @PostMapping("/update/selective/{id}")
    public ResponseJson<Void> updateMenuSelective(@PathVariable("id") Integer id, @RequestBody SysMenu menu) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysMenuService.updateMenuSelective(id, menu);
    }

    /**
     * 添加菜单
     */
    @PostMapping("/create")
    public ResponseJson<Void> addMenu(@RequestBody SysMenu menu) {
        return sysMenuService.addMenu(menu);
    }

    /**
     * 根据ID删除菜单
     */
    @PostMapping("/delete/{id}")
    public ResponseJson<Void> deleteMenu(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysMenuService.deleteMenu(id);
    }
}
