package com.von.controller.admin;

import com.von.entity.BlogCategory;
import com.von.service.CategoryService;
import com.von.util.PageQueryUtil;
import com.von.util.Result;
import com.von.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Scanner;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Resource
    private CategoryService categoryService;



    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request){
        request.setAttribute("path", "categories");
        return "admin/category";//跳转到admin下的category.html
    }

    //分类列表,每一个返回结果都统一使用 Result 类进行包装之后再返回给前端，并使用 @ResponseBody 注解将其转换为 json 格式
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if (org.springframework.util.StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //传入MAP对象pageQueryUtil，它包含两个参数start、limit，根据两个参数取数据
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(categoryService.getBlogCategoryPage(pageQueryUtil));
    }

    //分类添加
    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,@RequestParam("categoryIcon") String categoryIcon){
        if(StringUtils.isEmpty(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称");
        }
        if(StringUtils.isEmpty(categoryIcon)){
            return ResultGenerator.genFailResult("请选择分类图标");
        }
        if(categoryService.saveCategory(categoryName, categoryIcon)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    //分类修改
    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,@RequestParam("categoryName") String categoryName,@RequestParam("categoryIcon") String categoryIcon){
        if (categoryId == null || categoryId < 1){
            return ResultGenerator.genFailResult("非法参数！");
        }

        if(StringUtils.isEmpty(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称！");
        }

        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }

        if(categoryService.updateCategory(categoryId, categoryName, categoryIcon)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    //分类详情
    @GetMapping("/categories/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id){
        if(id == null || id < 1){
            return ResultGenerator.genFailResult("非法参数");
        }
        BlogCategory category = categoryService.selectById(id);
        return ResultGenerator.genSuccessResult(category);
    }

/*   分类删除
    这里的“删除”功能并不是真正意义上的删除，而是逻辑删除，接受的参数设置为一个数组，
    可以同时删除多条记录，只需要在前端将用户选择的记录 id 封装好再传参到后端即可
    使用 @RequestBody 将前端传过来的参数封装为 id 数组，参数验证通过后则调用 deleteBatch() 批量删除方法
    */
    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(categoryService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}








