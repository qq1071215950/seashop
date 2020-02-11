package com.haojing.service.impl;

import com.haojing.bo.CategoryBO;
import com.haojing.entity.Category;
import com.haojing.mapper.CategoryMapper;
import com.haojing.mapper.CategoryMapperCustom;
import com.haojing.service.CategoryService;
import com.haojing.vo.CategoryVO;
import com.haojing.vo.NewItemsVO;
import io.swagger.models.auth.In;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> result =  categoryMapper.selectByExample(example);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rootCatId", rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean queryCategoryIsExit(String catId) {
        Category category = categoryMapper.selectByPrimaryKey(catId);
        return category==null ? false: true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addCategory(CategoryBO categoryBO) {
        // todo 测试发现数据没有插入到数据中，后续处理
        // 添加的是一级分类
        Category category = new Category();
        if (categoryBO.getType() == 1){
            BeanUtils.copyProperties(categoryBO, category);
            System.out.println(category.toString());
            category.setFatherId(0);
            int i = categoryMapperCustom.insertCategory(category);
            if (i != 1){
                throw new RuntimeException("添加分类失败");
            }
        }
        // 添加的是二级 三级分类
        if (category.getType() == 2 || category.getType() == 3) {
            if (categoryBO.getFatherId() == null || categoryBO.getFatherId() == 0) {
                throw new RuntimeException("子分类的父分类id为空");
            }
            Category category1 = categoryMapper.selectByPrimaryKey(categoryBO.getFatherId());
            if (category1 == null) {
                throw new RuntimeException("父级分类信息不存在");
            }
            BeanUtils.copyProperties(categoryBO, category);
            int i = categoryMapperCustom.insertCategory(category);
            if (i != 1){
                throw new RuntimeException("添加分类失败");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteCategoryById(Integer categoryId) {
        this.deleteCategory(categoryId);
    }
    private void deleteCategory(Integer categoryId){
        // 1 先查询分类信息，判断是鸡鸡分类
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        System.out.println(category.toString());
        // 2 是三级分类直接删除
        if (category != null && category.getType() == 3){
            categoryMapper.deleteByPrimaryKey(categoryId);
        }
        // todo 目前设置三级分类，如何超过三级类，这里需要改动，bug
        // 3 是一二级分类，要循环查询所属子分类，递归删除
        if (category != null || category.getType() == 2 || category.getType() == 1){
            Example example = new Example(Category.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("fatherId", categoryId);
            List<Category> result =  categoryMapper.selectByExample(example);
            categoryMapper.deleteByPrimaryKey(categoryId);
            for (Category category1: result) {
                this.deleteCategory(category1.getId());
            }
        }
    }
}

