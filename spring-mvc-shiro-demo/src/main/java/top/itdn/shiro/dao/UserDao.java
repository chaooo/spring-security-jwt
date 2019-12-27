package top.itdn.shiro.dao;

import org.apache.ibatis.annotations.Mapper;
import top.itdn.shiro.entity.User;

import java.util.List;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getUserByUserName(String userName);
    List<String> queryRolesByUserName(String userName);
}