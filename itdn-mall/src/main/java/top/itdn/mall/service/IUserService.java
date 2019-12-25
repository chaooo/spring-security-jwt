package top.itdn.mall.service;

import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.entity.User;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
public interface IUserService {
    ResponseJson login(String username, String password);
    ResponseJson signUp(User user);
}
