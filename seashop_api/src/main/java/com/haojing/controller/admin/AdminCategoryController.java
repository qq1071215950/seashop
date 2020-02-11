package com.haojing.controller.admin;

import com.haojing.bo.CategoryBO;
import com.haojing.result.ResponseResult;
import com.haojing.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "分类管理", tags = {"管理员操作分类管理相关接口"})
@RestController
@RequestMapping("category")
@CrossOrigin
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "添加分类", notes = "添加", httpMethod = "POST")
    @PostMapping("/add")
    public ResponseResult addCategory(@RequestBody CategoryBO categoryBO) {
        if (categoryBO.getType() == null || categoryBO.getType() == 0) {
            return ResponseResult.errorMsg("分类类型错误");
        }
        categoryService.addCategory(categoryBO);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "删除分类信息", notes = "删除分类信息", httpMethod = "PUT")
    @PutMapping("/delete")
    public ResponseResult delete(
            @ApiParam(name = "categoryId", value = "分类id", required = true)
            @RequestParam Integer categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseResult.ok();
    }
}
