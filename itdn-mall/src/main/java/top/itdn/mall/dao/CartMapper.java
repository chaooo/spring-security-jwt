package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    Cart selectByPrimaryKey(Integer id);

    List<Cart> selectAll();

    int updateByPrimaryKey(Cart record);
}