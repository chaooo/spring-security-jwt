package com.example.jwt.config.security;

import com.example.jwt.dao.SysUserDao;
import com.example.jwt.entity.SysUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static java.util.Collections.emptyList;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserDao sysUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户" + username + "不存在!");
        }
        return new User(user.getUsername(), user.getPassword(), emptyList());
    }
}
