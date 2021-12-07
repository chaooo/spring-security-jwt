package com.example.jwt.dao;

import com.example.jwt.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Mapper
public interface SysUserDao {
    SysUser findByUsername(String username);

    void insertSysUser(SysUser sysUser);
}
