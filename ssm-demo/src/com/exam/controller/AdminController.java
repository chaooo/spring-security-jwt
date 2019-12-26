package com.exam.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exam.entity.Admin;
import com.exam.service.AdminService;



@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService as;
	@RequestMapping("/tologin")
	public String toLogin() {
		return "admin/login";
	}
	@RequestMapping("/tohome")
	public String toHome() {
		return "admin/home";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public boolean addUser(Admin admin, HttpServletRequest request) {
		//System.out.println(admin.getName()+"---"+admin.getPassword());
		boolean bl = as.Login(admin.getName(), admin.getPassword());
		if(bl) {
			//登录成功的逻辑
			request.getSession().setAttribute("admin", admin);
			return true;
		}
		//登录失败的逻辑
		request.setAttribute("msg", "登录失败");
		return false;
	}

}
