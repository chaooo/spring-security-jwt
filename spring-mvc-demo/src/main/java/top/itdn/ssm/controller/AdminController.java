package top.itdn.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import top.itdn.ssm.entity.Admin;
import top.itdn.ssm.service.AdminService;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/26
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value="/login",method=RequestMethod.POST)
    @ResponseBody
    public String addUser(Admin admin) {
        System.out.println("login:"+admin);
        boolean bl = adminService.Login(admin.getName(), admin.getPassword());
        if(bl) {
            //登录成功的逻辑
            return "Login Success!";
        }
        //登录失败的逻辑
        return "Login Failed!";
    }
}