package top.itdn.mall.dao;

import java.util.List;
import top.itdn.mall.entity.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    Category selectByPrimaryKey(Integer id);

    List<Category> selectAll();

    int updateByPrimaryKey(Category record);

    int updateByPrimaryKeySelective(Category record);

    List<Category> selectCategoryChildrenByParentId(Integer categoryId);
}