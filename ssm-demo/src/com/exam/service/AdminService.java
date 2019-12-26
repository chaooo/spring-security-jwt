package com.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.mapper.AdminDao;

@Service("adminService")
public class AdminService {
	@Autowired
	private AdminDao dao;

	public boolean Login(String name, String password) {
		try {
			return dao.findByNameAndPassword(name, password)!=null?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
