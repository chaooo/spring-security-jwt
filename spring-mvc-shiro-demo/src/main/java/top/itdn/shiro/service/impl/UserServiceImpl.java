package top.itdn.shiro.service.impl;

import org.springframework.stereotype.Service;
import top.itdn.shiro.dao.UserDao;
import top.itdn.shiro.entity.User;
import top.itdn.shiro.service.UserService;

import javax.annotation.Resource;
import java.util.Set;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public User getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}

	@Override
	public Set<String> getRoles(String userName) {
		return userDao.getRoles(userName);
	}

	@Override
	public Set<String> getPermissions(String userName) {
		return userDao.getPermissions(userName);
	}

}
