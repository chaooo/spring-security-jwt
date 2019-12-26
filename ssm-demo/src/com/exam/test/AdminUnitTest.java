package com.exam.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.exam.entity.Admin;
import com.exam.mapper.AdminDao;
import com.exam.test.base.BaseJunit4Test;


public class AdminUnitTest extends BaseJunit4Test {

	@Autowired
	private AdminDao dao;
	
	@Test
	public void testAdminDao() {
		Admin admin = dao.findByNameAndPassword("admin", "123456");
		System.out.println(admin);
	}
}