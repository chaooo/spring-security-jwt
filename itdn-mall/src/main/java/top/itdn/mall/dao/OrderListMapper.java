package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.OrderList;

public interface OrderListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderList record);

    OrderList selectByPrimaryKey(Integer id);

    List<OrderList> selectAll();

    int updateByPrimaryKey(OrderList record);
}