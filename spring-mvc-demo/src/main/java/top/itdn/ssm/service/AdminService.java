package top.itdn.ssm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itdn.ssm.dao.AdminDao;
import top.itdn.ssm.entity.Admin;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/26
 */
@Service
public class AdminService {
    @Autowired
    private AdminDao dao;

    public boolean Login(String name, String password) {
        try {
            Admin admin = dao.findByNameAndPassword(name, password);
            return admin != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
