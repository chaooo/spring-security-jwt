package top.itdn.mall.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itdn.mall.common.Const;
import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.entity.User;
import top.itdn.mall.service.IUserService;
import top.itdn.mall.util.CookieUtil;
import top.itdn.mall.util.JsonUtil;
import top.itdn.mall.util.RedisShardedPoolUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */

@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ResponseJson response = iUserService.login(username, password);
        if(response.getStatus()==0){
            //登录成功
            User user = (User) response.getData();
            //user对象不返回到api接口
            response.setData(null);
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                //CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                //RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.REDIS_SESSION_EXTIME);
            }else{
                response.setMsg("不是管理员,无法登录");
            }
        }
        return response;
    }
    @RequestMapping(value="sign.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson sign(User user, HttpSession session, HttpServletResponse httpServletResponse){
        ResponseJson response = iUserService.signUp(user);
        if(response.getStatus()==0) {
            //注册成功
            //CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            //RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.REDIS_SESSION_EXTIME);
        }
        return response;
    }

}
