package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysRole;
import com.rabbiter.cm.mapper.SysRoleMapper;
import com.rabbiter.cm.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
////SysRoleServiceImpl 类是一个服务实现类，它使用 Spring 的 @Service 注解被 Spring 容器管理，实现了 SysRoleService 接口中定义的服务方法，
// 通过 @Autowired 注入了 SysRoleMapper 进行数据库操作。这些服务方法包括查找角色、添加角色、更新角色、删除角色和权限分配，
// 遵循了 Spring 框架的依赖注入和服务层开发的基本模式，将业务逻辑和数据库操作解耦，提高了代码的可维护性和可扩展性。

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> findAllRoles() {
        return sysRoleMapper.findAllRoles();
    }

    @Override
    public SysRole findRoleById(Long id) {
        return sysRoleMapper.findRoleById(id);
    }

    @Override
    public int addRole(SysRole sysRole) {
        return sysRoleMapper.addRole(sysRole);
    }

    @Override
    public int updateRole(SysRole sysRole) {
        return sysRoleMapper.updateRole(sysRole);
    }

    @Override
    public int deleteRole(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysRoleMapper.deleteRole(id);
        }
        return rows;
    }

    @Override
    public int allotRight(Long roleId, Long[] keys) {
        int rows = 0;
        HashSet<Long> originResources = new HashSet<>(sysRoleMapper.findAllRights(roleId));

        for (Long id : keys) {
            if (originResources.contains(id)) {
                originResources.remove(id);
            } else {
                rows += sysRoleMapper.addRight(roleId, id);
            }
        }
        for (Long id : originResources) {
            rows += sysRoleMapper.deleteRight(roleId, id);
        }
        return rows;
    }
}
