package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysResource;
import com.rabbiter.cm.mapper.SysResourceMapper;
import com.rabbiter.cm.service.SysResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
///SysResourceServiceImpl 类将资源管理的业务逻辑封装在服务层，通过调用数据访问层的方法完成对资源信息的查询、添加、更新和删除操作，
// 同时处理资源的层级关系设置，使用日志进行信息记录和问题排查，为系统中的资源管理提供了完善的服务功能，实现了业务逻辑和数据访问逻辑的分离，
// 增强了代码的可维护性和系统的可管理性。

@Service
public class SysResourceServiceImpl implements SysResourceService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Override
    public List<SysResource> findAllResources() {
        return sysResourceMapper.findAllResources();
    }

    @Override
    public List<SysResource> findWithChildren() {
        return sysResourceMapper.findWithChildren();
    }

    @Override
    public List<SysResource> findAllWithAllChildren() {
        return sysResourceMapper.findAllWithAllChildren();
    }

    @Override
    public SysResource findResourceById(Long id) {
        return sysResourceMapper.findResourceById(id);
    }

    @Override
    public int addResource(SysResource sysResource) {
        if (sysResource.getParentId() == 0) {
            sysResource.setLevel(1);
        } else {
            SysResource parent = this.findResourceById(sysResource.getParentId());
            if (parent != null) {
                sysResource.setLevel(parent.getLevel() + 1);
            }
        }
        return sysResourceMapper.addResource(sysResource);
    }

    @Override
    public int updateResource(SysResource sysResource) {
        if (sysResource.getParentId() == 0) {
            sysResource.setLevel(1);
        } else {
            SysResource parent = this.findResourceById(sysResource.getParentId());
            if (parent != null) {
                sysResource.setLevel(parent.getLevel() + 1);
            }
        }
        log.debug(sysResource.toString());
        return sysResourceMapper.updateResource(sysResource);
    }

    @Override
    public int deleteResource(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysResourceMapper.deleteResource(id);
        }
        return rows;
    }
}
