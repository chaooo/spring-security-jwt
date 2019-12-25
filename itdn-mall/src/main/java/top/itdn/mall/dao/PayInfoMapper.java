package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.PayInfo;

public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    List<PayInfo> selectAll();

    int updateByPrimaryKey(PayInfo record);
}