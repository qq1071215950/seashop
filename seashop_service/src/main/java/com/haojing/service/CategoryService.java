package com.haojing.service;

import com.haojing.bo.CategoryBO;
import com.haojing.entity.Category;
import com.haojing.vo.CategoryVO;
import com.haojing.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有的一级分类
     * @return
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类获取子分类
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 根据一级分类id获取最新6个商品信息
     * @param rootCatId
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(@Param("rootCatId") Integer rootCatId);


    /**
     * 查询分类信息是否存在
     * @param catId
     * @return
     */
    Boolean queryCategoryIsExit(String catId);

    /**
     * 添加分类
     * @param categoryBO
     */
    void addCategory(CategoryBO categoryBO);


    /**
     * 通过id删除分类信息
     * @param categoryId
     */
    void deleteCategoryById(Integer categoryId);
}
