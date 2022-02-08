package com.example.jwt.config.security;

import com.example.jwt.dao.SystemDao;
import com.example.jwt.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SystemDao systemDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = systemDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户" + username + "不存在!");
        }
        // 获取用户角色列表
        List<String> roleNames = systemDao.getRoleNamesByUserId(user.getId());
        // 设置权限和角色
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roleNames) {
            if (StringUtils.hasLength(role)){
                authorities.add( new GrantedAuthorityImpl(role));
            }
        }
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}

