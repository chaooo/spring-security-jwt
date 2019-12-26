package com.exam.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exam.service.AdminService;





public class AdminTest {

	public static void main(String[] args) {
		ApplicationContext app = new  ClassPathXmlApplicationContext("applicationContext.xml");
		AdminService service = app.getBean("adminService", AdminService.class);
		boolean flag = service.Login("admin", "123456");
		System.out.println(flag?"登录成功":"登录失败");
	}

}
