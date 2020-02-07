package com.haojing.service;

import com.haojing.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有的一级分类
     * @return
     */
    List<Category> queryAllRootLevelCat();
}
