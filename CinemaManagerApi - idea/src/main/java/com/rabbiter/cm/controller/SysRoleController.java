package com.rabbiter.cm.controller;

import com.rabbiter.cm.common.response.ResponseResult;
import com.rabbiter.cm.domain.SysRole;
import com.rabbiter.cm.service.impl.SysRoleServiceImpl;
import com.rabbiter.cm.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
////SysRoleController 是一个 Spring 的控制器类，使用 @RestController 注解标记，继承了 BaseController。
// 通过 @Autowired 注入 SysRoleServiceImpl 服务类，使用不同的请求映射注解将不同的请求方法映射到不同的 HTTP 请求路径，
// 处理对角色的查找、添加、更新、删除和权限分配操作，并将服务层的操作结果包装在 ResponseResult 中返回，实现了控制器层和服务层的交互，
@RestController
public class SysRoleController extends BaseController {

    @Autowired
    SysRoleServiceImpl sysRoleService;

    @GetMapping("/sysRole")
    public ResponseResult findAllRoles() {
        startPage();
        List<SysRole> data = sysRoleService.findAllRoles();
        return getResult(data);
    }

    @GetMapping("/sysRole/{id}")
    public ResponseResult findRoleById(@PathVariable Long id) {
        return getResult(sysRoleService.findRoleById(id));
    }

    @PostMapping("/sysRole")
    public ResponseResult addRole(@Validated @RequestBody SysRole sysRole) {
        return getResult(sysRoleService.addRole(sysRole));
    }

    @PutMapping("/sysRole")
    public ResponseResult updateRole(@Validated @RequestBody SysRole sysRole) {
        return getResult(sysRoleService.updateRole(sysRole));
    }

    @DeleteMapping("/sysRole/{ids}")
    public ResponseResult deleteRole(@PathVariable Long[] ids) {
        return getResult(sysRoleService.deleteRole(ids));
    }

    /**
     * 给指定 id 的角色分配权限，包括增加或者删除权限
     * @param roleId
     * @param keys
     * @return
     */
    @PostMapping("/sysRole/{roleId}")
    public ResponseResult allotRight(@PathVariable Long roleId, @RequestBody Long[] keys) {
        return getResult(sysRoleService.allotRight(roleId, keys));
    }

}
