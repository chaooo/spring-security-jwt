package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    List<OrderItem> selectAll();

    int updateByPrimaryKey(OrderItem record);
}