package com.example.jwt.service.impl;

import com.example.jwt.dao.SystemDao;
import com.example.jwt.entity.*;
import com.example.jwt.service.RedisService;
import com.example.jwt.service.SysUserService;
import com.example.jwt.util.CommonUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private SystemDao systemDao;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private RedisService redisService;

    /**
     * 获取用户信息
     */
    @Override
    public ResponseJson<SysUser> getInfoByUsername(String username) {
        if (StringUtils.hasLength(username)) {
            SysUser sysUser = systemDao.findByUsername(username);
            if (null != sysUser) {
                List<Integer> roleIds = setUserRoleInfo(sysUser);
                List<SysMenu> menus = null;
                if (!CollectionUtils.isEmpty(roleIds)) {
                    // 根据角色Id获取菜单列表
                    menus = systemDao.getMenusByRoleIds(roleIds);
                    // 根据角色Id获取按钮权限列表
                    List<String> permissions = systemDao.getPermissionNames(roleIds);
                    sysUser.setPermissions(permissions);
                }
                sysUser.setMenus(menus);
            }

            return ResponseJson.success(sysUser);
        }
        return ResponseJson.error("用户数据异常", null);
    }

    private List<Integer> setUserRoleInfo(SysUser sysUser) {
        List<Integer> roleIds = new ArrayList<>();
        if (null != sysUser) {
            List<String> roleNames = new ArrayList<>();
            StringBuilder roleDesc = new StringBuilder();
            // 根据用户Id获取用户角色列表
            List<SysRole> roleList = systemDao.getRoleListByUserId(sysUser.getId());
            if (!CollectionUtils.isEmpty(roleList)) {
                for (SysRole role : roleList) {
                    if (null != role) {
                        roleNames.add(role.getRoleName());
                        roleDesc.append(role.getRoleDesc()).append(" ");
                        roleIds.add(role.getId());
                    }
                }
            }
            sysUser.setRoles(roleNames);
            sysUser.setRoleDesc(roleDesc.toString());
        }
        return roleIds;
    }

    /**
     * 退出登录
     */
    @Override
    public ResponseJson<Void> logout(String token) {
        if (StringUtils.hasLength(token)) {
            redisService.remove(token);
        }
        return ResponseJson.success();
    }

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param fullName 姓名
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseJson<PaginationVo<SysUser>> getUserList(String username, String fullName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> userList = systemDao.getUserList(username, fullName);
        for (SysUser sysUser : userList) {
            List<Integer> roleIdList = setUserRoleInfo(sysUser);
            String roleIds = CommonUtil.idListToString(roleIdList, ",");
            sysUser.setRoleIds(roleIds);
        }
        PaginationVo<SysUser> pageData = new PaginationVo<>(userList);
        return ResponseJson.success(pageData);
    }

    /**
     * 根据ID获取用户
     */
    @Override
    public ResponseJson<SysUser> getUser(Integer id) {
        SysUser sysUser = systemDao.getUser(id);
        sysUser.setPassword("");
        List<Integer> roleIdList = setUserRoleInfo(sysUser);
        String roleIds = CommonUtil.idListToString(roleIdList, ",");
        sysUser.setRoleIds(roleIds);
        return ResponseJson.success(sysUser);
    }

    /**
     * 根据ID更新用户
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> updateUser(Integer id, SysUser sysUser) {
        sysUser.setId(id);
        sysUser.setDelFlag(0);
        if (StringUtils.hasLength(sysUser.getPassword())){
            // 密码加密
            String encodePassword = bCryptPasswordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(encodePassword);
        }
        // 删除原有角色关联
        systemDao.deleteUserRoleRelation(id);
        // 前端传入角色Ids，用","隔开
        String roleIds = sysUser.getRoleIds();
        // 保存用户角色关系
        saveRoleRelation(sysUser.getId(), roleIds);
        systemDao.updateSysUser(sysUser);
        return ResponseJson.success();
    }

    /**
     * 添加用户
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> addUser(SysUser sysUser) {
        if (StringUtils.hasLength(sysUser.getUsername()) && StringUtils.hasLength(sysUser.getPassword())) {
            // 查询是否存在当前用户名的用户
            SysUser dbUser = systemDao.findByUsername(sysUser.getUsername());
            if (null == dbUser || dbUser.getDelFlag() != 0) {
                // 密码加密
                String encodePassword = bCryptPasswordEncoder.encode(sysUser.getPassword());
                sysUser.setPassword(encodePassword);
                // 新增用户
                systemDao.insertSysUser(sysUser);
                // 角色Ids，用","隔开
                String roleIds = sysUser.getRoleIds();
                // 保存用户角色关系
                saveRoleRelation(sysUser.getId(), roleIds);
                return ResponseJson.success("添加用户成功", null);
            } else if (!Integer.valueOf(0).equals(dbUser.getDelFlag())){
                // 更新已删除用户
                return updateUser(dbUser.getId(), sysUser);
            } else {
                return ResponseJson.error("用户名已存在", null);
            }
        }
        return ResponseJson.error("用户名或密码不能为空", null);
    }

    /**
     * 根据ID删除用户
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> deleteUser(Integer id) {
        // 删除用户角色关联
        systemDao.deleteUserRoleRelation(id);
        // 删除用户
        systemDao.deleteSysUser(id);
        return ResponseJson.success();
    }

    /**
     * 保存用户角色关系
     */
    private void saveRoleRelation(Integer userId, String roleIds) {
        if (StringUtils.hasLength(roleIds)) {
            // 设置用户角色
            String[] split = roleIds.split(",");
            for (String s : split) {
                if (StringUtils.hasLength(s)) {
                    // 保存用户角色关系
                    systemDao.insertUserRoleRelation(userId, Integer.valueOf(s));
                }
            }
        }
    }

    /**
     * 用户修改资料
     */
    @Override
    public ResponseJson<Void> updateUserProfile(UserProfile profile) {
        SysUser sysUser = systemDao.getUser(profile.getUserId());
        if (null == sysUser) {
            return ResponseJson.error("用户异常！", null);
        }
        if (StringUtils.hasLength(profile.getUsername())){
            sysUser.setUsername(profile.getUsername());
        }
        if (StringUtils.hasLength(profile.getFullName())){
            sysUser.setFullName(profile.getFullName());
        }
        if (StringUtils.hasLength(profile.getPhone())){
            sysUser.setPhone(profile.getPhone());
        }
        if (StringUtils.hasLength(profile.getAvatar())){
            sysUser.setAvatar(profile.getAvatar());
        }
        systemDao.updateSysUser(sysUser);
        return ResponseJson.success();
    }

    /**
     * 用户修改密码
     */
    @Override
    public ResponseJson<Void> updateUserPassword(UserProfile profile) {
        SysUser sysUser = systemDao.getUser(profile.getUserId());
        if (null == sysUser) {
            return ResponseJson.error("用户异常！", null);
        }
        if (!StringUtils.hasLength(profile.getPassword())){
            return ResponseJson.error("新密码不能为空！", null);
        }
        if (!profile.getPassword().equals(profile.getConfirmPassword())){
            return ResponseJson.error("两次密码输入不一致！", null);
        }
        if (!bCryptPasswordEncoder.matches(profile.getOldPassword(), sysUser.getPassword())) {
            return ResponseJson.error("旧密码输入错误！", null);
        }
        // 密码加密
        String encodePassword = bCryptPasswordEncoder.encode(profile.getPassword());
        sysUser.setPassword(encodePassword);
        systemDao.updateSysUser(sysUser);
        return ResponseJson.success();
    }
}
