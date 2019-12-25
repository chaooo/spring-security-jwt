package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    List<Shipping> selectAll();

    int updateByPrimaryKey(Shipping record);
}