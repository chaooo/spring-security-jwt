package com.example.jwt.service.impl;

import com.example.jwt.dao.SysUserDao;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysMenu;
import com.example.jwt.entity.SysUser;
import com.example.jwt.service.RedisService;
import com.example.jwt.service.SysUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/10
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private RedisService redisService;

    @Override
    public ResponseJson<SysUser> register(SysUser sysUser) {
        if (StringUtils.hasLength(sysUser.getUsername()) && StringUtils.hasLength(sysUser.getPassword())) {
            // 密码加密
            String encodePassword = passwordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(encodePassword);
            // 新增用户
            sysUserDao.insertSysUser(sysUser);
            // 角色Ids，用","隔开
            String roleIds = sysUser.getRoleIds();
            if (StringUtils.hasLength(roleIds)) {
                // 设置用户角色
                String[] split = roleIds.split(",");
                for (String s : split) {
                    if (StringUtils.hasLength(s)) {
                        // 保存用户角色关系
                        sysUserDao.insertUserRoleRelation(sysUser.getId(), Long.valueOf(s));
                    }
                }
            }
            return ResponseJson.success("注册成功", sysUser);
        }
        return ResponseJson.error("用户名或密码不能为空", null);
    }

    /**
     * 获取用户可访问菜单
     */
    @Override
    public ResponseJson<List<SysMenu>> menuList(String username) {
        if (!StringUtils.hasLength(username)) {
            return ResponseJson.error("用户信息异常", null);
        }
        // 获取用户角色Id
        List<Long> roleIds = sysUserDao.getRoleIdsByUserId(username);
        List<SysMenu> menus = null;
        if (!CollectionUtils.isEmpty(roleIds)) {
            // 根据角色Id获取菜单列表
            menus = sysUserDao.getMenuListByRoleIds(roleIds);
        }
        return ResponseJson.success(menus);
    }
}
