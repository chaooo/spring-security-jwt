package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

}