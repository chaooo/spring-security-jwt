package com.example.jwt.controller;

import com.example.jwt.config.security.ConstantKey;
import com.example.jwt.config.security.JwtService;
import com.example.jwt.entity.*;
import com.example.jwt.service.SysUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@RestController
@RequestMapping("/sys")
public class SysUserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private JwtService jwtService;

    /**
     * 获取用户信息
     */
    @GetMapping("/user/info")
    public ResponseJson<SysUser> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        String username = jwtService.getUsername(token);
        return sysUserService.getInfoByUsername(username);
    }

    /**
     * 退出登录
     */
    @PostMapping("/user/logout")
    public ResponseJson<Void> logout(HttpServletRequest request) {
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        return sysUserService.logout(token);
    }

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param fullName 姓名
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/user/list")
    public ResponseJson<PaginationVo<SysUser>> userList(String username, String fullName,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysUserService.getUserList(username, fullName, pageNum, pageSize);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/user/{id}")
    public ResponseJson<SysUser> getUser(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.getUser(id);
    }

    /**
     * 根据ID更新用户
     */
    @PostMapping("/user/update/{id}")
    public ResponseJson<Void> updateUser(@PathVariable("id") Integer id, @RequestBody SysUser sysUser) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.updateUser(id, sysUser);
    }

    /**
     * 添加用户
     */
    @PostMapping("/user/create")
    public ResponseJson<Void> addUser(@RequestBody SysUser sysUser) {
        return sysUserService.addUser(sysUser);
    }

    /**
     * 根据ID删除用户
     */
    @PostMapping("/user/delete/{id}")
    public ResponseJson<Void> deleteUser(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.deleteUser(id);
    }

    /**
     * 获取角色列表
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/role/list")
    public ResponseJson<PaginationVo<SysRole>> roleList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysUserService.getRoleList(pageNum, pageSize);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/role/{id}")
    public ResponseJson<SysRole> getRole(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysUserService.getRoleInfo(id);
    }

    /**
     * 根据ID更新角色
     */
    @PostMapping("/role/update/{id}")
    public ResponseJson<Void> updateRole(@PathVariable("id") Integer id, @RequestBody SysRole sysRole) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysUserService.updateRole(id, sysRole);
    }

    /**
     * 添加角色
     */
    @PostMapping("/role/create")
    public ResponseJson<Void> addRole(@RequestBody SysRole sysRole) {
        return sysUserService.addRole(sysRole);
    }

    /**
     * 根据ID删除角色
     */
    @PostMapping("/role/delete/{id}")
    public ResponseJson<Void> deleteRole(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysUserService.deleteRole(id);
    }

    /**
     * 角色授权菜单
     */
    @PostMapping("/role/auth/{id}")
    public ResponseJson<Void> setRoleMenu(@PathVariable("id") Integer id, @RequestBody SysRole sysRole) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        if (!StringUtils.hasLength(sysRole.getMenuIds())){
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysUserService.setRoleMenu(id, sysRole);
    }

    /**
     * 获取菜单列表
     *
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/menu/list")
    public ResponseJson<PaginationVo<SysMenu>> menuList(Integer status,
            @RequestParam(value = "parentId", defaultValue = "0") Integer parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysUserService.getMenuList(parentId, status, pageNum, pageSize);
    }

    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    @GetMapping("/menu/tree")
    public ResponseJson<List<SysMenu>> menuTree() {
        return sysUserService.getMenuTree();
    }

    /**
     * 根据ID获取菜单
     */
    @GetMapping("/menu/{id}")
    public ResponseJson<SysMenu> getMenu(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysUserService.getMenu(id);
    }

    /**
     * 根据ID更新菜单
     */
    @PostMapping("/menu/update/{id}")
    public ResponseJson<Void> updateMenu(@PathVariable("id") Integer id, @RequestBody SysMenu menu) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysUserService.updateMenu(id, menu);
    }

    /**
     * 根据ID(可选择的)更新菜单字段
     */
    @PostMapping("/menu/update/selective/{id}")
    public ResponseJson<Void> updateMenuSelective(@PathVariable("id") Integer id, @RequestBody SysMenu menu) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysUserService.updateMenuSelective(id, menu);
    }

    /**
     * 添加菜单
     */
    @PostMapping("/menu/create")
    public ResponseJson<Void> addMenu(@RequestBody SysMenu menu) {
        return sysUserService.addMenu(menu);
    }

    /**
     * 根据ID删除菜单
     */
    @PostMapping("/menu/delete/{id}")
    public ResponseJson<Void> deleteMenu(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysUserService.deleteMenu(id);
    }

}
