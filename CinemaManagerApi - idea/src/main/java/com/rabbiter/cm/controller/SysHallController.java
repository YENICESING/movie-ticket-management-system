package com.rabbiter.cm.controller;

import com.rabbiter.cm.common.response.ResponseResult;
import com.rabbiter.cm.domain.SysHall;
import com.rabbiter.cm.service.impl.SysHallServiceImpl;
import com.rabbiter.cm.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
////////该类位于 com.rabbiter.cm.controller 包中，使用 @RestController 注解，
// 表明它是一个 Spring RESTful 控制器，用于处理 HTTP 请求并返回 JSON 响应，继承自 BaseController
///@GetMapping("/sysHall")：处理 GET 请求，调用 startPage() 方法（可能是分页相关操作），然后调用 sysHallService.findAllHalls(sysHall) 方法查询厅信息，并将结果通过 getResult() 方法封装为 ResponseResult 并返回。
//@GetMapping("/sysHall/{hallId}")：处理 GET 请求，根据 @PathVariable 获取 hallId，创建 SysHall 对象并设置 hallId，调用 sysHallService.findHallById(sysHall) 方法查询厅信息，并将结果通过 getResult() 方法封装为 ResponseResult 并返回。
//@PostMapping("/sysHall")：处理 POST 请求，使用 @Validated 注解对请求体中的 SysHall 对象进行数据校验，调用 sysHallService.addHall(sysHall) 方法添加厅信息，并将结果通过 getResult() 方法封装为 ResponseResult 并返回。
//@PutMapping("/sysHall")：处理 PUT 请求，使用 @Validated 注解对请求体中的 SysHall 对象进行数据校验，调用 sysHallService.updateHall(sysHall) 方法更新厅信息，并将结果通过 getResult() 方法封装为 ResponseResult 并返回。
//@PostMapping("/sysHall/delete")：处理 POST 请求，接收 @RequestBody 中的 SysHall[] 数组，调用 sysHallService.deleteHall(sysHalls) 方法删除厅信息，并将结果通过 getResult() 方法封装为 ResponseResult 并返回。
@RestController
public class SysHallController extends BaseController {

    @Autowired
    private SysHallServiceImpl sysHallService;

    @GetMapping("/sysHall")
    public ResponseResult findAllHalls(SysHall sysHall) {
        startPage();
        return getResult(sysHallService.findAllHalls(sysHall));
    }

    @GetMapping("/sysHall/{hallId}")
    public ResponseResult findHallById(@PathVariable Long hallId) {
        SysHall sysHall = new SysHall();
        sysHall.setHallId(hallId);
        return getResult(sysHallService.findHallById(sysHall));
    }

    @PostMapping("/sysHall")
    public ResponseResult addHall(@Validated @RequestBody SysHall sysHall) {
        return getResult(sysHallService.addHall(sysHall));
    }

    @PutMapping("/sysHall")
    public ResponseResult updateHall(@Validated @RequestBody SysHall sysHall) {
        int rows = sysHallService.updateHall(sysHall);
        return getResult(rows);
    }

    @PostMapping("/sysHall/delete")
    public ResponseResult deleteHall(@RequestBody SysHall[] sysHalls) {
        return getResult(sysHallService.deleteHall(sysHalls));
    }

}
