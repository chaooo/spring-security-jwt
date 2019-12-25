package top.itdn.mall.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.service.ICategoryService;
import top.itdn.mall.service.IUserService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {


    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ResponseJson addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.addCategory(categoryName,parentId);
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ResponseJson setCategoryName(HttpServletRequest httpServletRequest,Integer categoryId,String categoryName){
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.updateCategoryName(categoryId,categoryName);
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ResponseJson getChildrenParallelCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ResponseJson getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }


}
