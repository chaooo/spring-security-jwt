package top.itdn.ssm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.itdn.ssm.entity.Admin;

@Mapper
public interface AdminDao {
    Admin findByNameAndPassword(@Param("name") String name, @Param("password") String password);
}