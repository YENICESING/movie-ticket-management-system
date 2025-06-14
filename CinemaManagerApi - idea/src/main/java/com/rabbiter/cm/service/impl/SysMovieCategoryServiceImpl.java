package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysMovieCategory;
import com.rabbiter.cm.domain.SysMovieToCategory;
import com.rabbiter.cm.mapper.SysMovieCategoryMapper;
import com.rabbiter.cm.service.SysMovieCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//////SysMovieCategoryServiceImpl 类将与电影类别相关的业务逻辑封装在服务层，为控制器层提供了方便的服务接口，
// 通过调用数据访问层实现对电影类别信息的查询、添加、更新、删除以及电影与类别关联关系的添加和删除操作，使系统能够对电影类别信息进行有效的管理和操作，
// 同时实现了业务逻辑和数据访问逻辑的分离，提高了代码的可维护性和可扩展性。
@Service
public class SysMovieCategoryServiceImpl implements SysMovieCategoryService {

    @Autowired
    private SysMovieCategoryMapper sysMovieCategoryMapper;

    @Override
    public List<SysMovieCategory> findAllCategorys() {
        return sysMovieCategoryMapper.findAllCategorys();
    }

    @Override
    public SysMovieCategory findCategoryById(Long id) {
        return sysMovieCategoryMapper.findCategoryById(id);
    }

    @Override
    public int addCategory(SysMovieCategory sysMovieCategory) {
        return sysMovieCategoryMapper.addCategory(sysMovieCategory);
    }

    @Override
    public int updateCategory(SysMovieCategory sysMovieCategory) {
        return sysMovieCategoryMapper.updateCategory(sysMovieCategory);
    }

    @Override
    public int deleteCategory(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysMovieCategoryMapper.deleteCategory(id);
        }
        return rows;
    }


    @Override
    public int addMovieToCategory(SysMovieToCategory sysMovieToCategory) {
        return sysMovieCategoryMapper.addMovieToCategory(sysMovieToCategory);
    }

    @Override
    public int deleteMovieToCategory(SysMovieToCategory sysMovieToCategory) {
        return sysMovieCategoryMapper.deleteMovieToCategory(sysMovieToCategory);
    }

}
