package top.itdn.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.itdn.mall.common.Const;
import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.dao.CategoryMapper;
import top.itdn.mall.entity.Category;
import top.itdn.mall.service.ICategoryService;

import java.util.List;
import java.util.Set;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public ResponseJson addCategory(String categoryName, Integer parentId) {
        ResponseJson response = new ResponseJson();
        if(parentId == null || StringUtils.isBlank(categoryName)){
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg("添加品类参数错误");
            return response;
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParent_id(parentId);
        category.setStatus(true);//这个分类是可用的
        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            //成功
            response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
            response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
        }else {
            //失败
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg(Const.MessageEnum.ERROR.getMsg());
        }
        return response;
    }

    @Override
    public ResponseJson updateCategoryName(Integer categoryId, String categoryName) {
        ResponseJson response = new ResponseJson();
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg("添加品类参数错误");
            return response;
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            //成功
            response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
            response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
        }else {
            //失败
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg(Const.MessageEnum.ERROR.getMsg());
        }
        return response;
    }

    @Override
    public ResponseJson getChildrenParallelCategory(Integer categoryId) {
        ResponseJson response = new ResponseJson();
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        response.setData(categoryList);
        if(!CollectionUtils.isEmpty(categoryList)){
            //成功
            response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
            response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
        }else {
            //失败
            response.setStatus(Const.MessageEnum.ERROR.getStatus());
            response.setMsg("未找到当前分类的子分类");
        }
        return response;
    }

    @Override
    public ResponseJson selectCategoryAndChildrenById(Integer categoryId) {
        ResponseJson response = new ResponseJson();
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        response.setData(categoryIdList);
        response.setStatus(Const.MessageEnum.SUCCESS.getStatus());
        response.setMsg(Const.MessageEnum.SUCCESS.getMsg());
        return response;
    }

    /**
     * 递归算法,算出子节点
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}

