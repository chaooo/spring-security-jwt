package com.caimei365.manager.controller.sys;

import com.caimei365.manager.entity.PaginationVo;
import com.caimei365.manager.entity.ResponseJson;
import com.caimei365.manager.entity.sys.SysRole;
import com.caimei365.manager.service.sys.SysRoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/1/6
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleApi {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取角色列表
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/list")
    public ResponseJson<PaginationVo<SysRole>> roleList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysRoleService.getRoleList(pageNum, pageSize);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    public ResponseJson<SysRole> getRole(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysRoleService.getRoleInfo(id);
    }

    /**
     * 根据ID更新角色
     */
    @PostMapping("/update/{id}")
    public ResponseJson<Void> updateRole(@PathVariable("id") Integer id, @RequestBody SysRole sysRole) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysRoleService.updateRole(id, sysRole);
    }

    /**
     * 添加角色
     */
    @PostMapping("/create")
    public ResponseJson<Void> addRole(@RequestBody SysRole sysRole) {
        return sysRoleService.addRole(sysRole);
    }

    /**
     * 根据ID删除角色
     */
    @PostMapping("/delete/{id}")
    public ResponseJson<Void> deleteRole(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        return sysRoleService.deleteRole(id);
    }

    /**
     * 角色授权菜单
     */
    @PostMapping("/auth/{id}")
    public ResponseJson<Void> setRoleMenu(@PathVariable("id") Integer id, @RequestBody SysRole sysRole) {
        if (null == id || id <= 0) {
            return ResponseJson.error("角色Id不能为空！", null);
        }
        if (!StringUtils.hasLength(sysRole.getMenuIds())){
            return ResponseJson.error("菜单Id不能为空！", null);
        }
        return sysRoleService.setRoleMenu(id, sysRole);
    }

}
