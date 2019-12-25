package top.itdn.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itdn.mall.common.Const;
import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.dao.UserMapper;
import top.itdn.mall.entity.User;
import top.itdn.mall.service.IUserService;
import top.itdn.mall.util.MD5Util;

import java.util.Date;


/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResponseJson login(String username, String password) {
        ResponseJson response = new ResponseJson();
        int userId = userMapper.checkUsername(username);
        if(userId == 0 ) {
            //用户名不存在
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg(Const.MessageEnum.ERROR.getMsg());
            return response;
        }
        User user = userMapper.selectByPrimaryKey(userId);
        //查询用户密码用于校验
        String dbPassword = user.getPassword();
        String md5Password = MD5Util.md5(password + user.getSalt());
        if(md5Password.equals(dbPassword)) {
            //成功
            response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
            response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
            response.setData(user);
        }else {
            //失败
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg(Const.MessageEnum.ERROR.getMsg());
        }
        return response;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public ResponseJson signUp(User user) {
        ResponseJson response = new ResponseJson();
        //注册前检查用户名是否被占用
        int userId = userMapper.checkUsername(user.getUsername());
        if(userId > 0 ) {
            //用户名被占用
            response.setStatus(Const.MessageEnum.NAME_OCCUPIED.getStatus());
            response.setMsg(Const.MessageEnum.NAME_OCCUPIED.getMsg());
            return response;
        }
        //密码加密后再保存
        String salt = MD5Util.salt();
        String md5Password = MD5Util.md5(user.getPassword()+salt);
        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setCreate_time(new Date());
        //添加到数据库
        int row = userMapper.insert(user);
        if(row>0) {
            //注册成功
            response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
            response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
        }else {
            //注册失败
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg(Const.MessageEnum.ERROR.getMsg());
        }
        return response;
    }
}
