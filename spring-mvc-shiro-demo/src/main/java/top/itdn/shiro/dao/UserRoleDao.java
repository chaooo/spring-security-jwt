package top.itdn.shiro.dao;

import org.apache.ibatis.annotations.Mapper;
import top.itdn.shiro.entity.UserRole;

@Mapper
public interface UserRoleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}