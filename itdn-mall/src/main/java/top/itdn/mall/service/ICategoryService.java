package top.itdn.mall.service;

import top.itdn.mall.common.ResponseJson;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
public interface ICategoryService {
    ResponseJson addCategory(String categoryName, Integer parentId);
    ResponseJson updateCategoryName(Integer categoryId,String categoryName);
    ResponseJson getChildrenParallelCategory(Integer categoryId);
    ResponseJson selectCategoryAndChildrenById(Integer categoryId);
}