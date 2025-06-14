package com.rabbiter.cm.controller;

import com.rabbiter.cm.common.exception.DataNotFoundException;
import com.rabbiter.cm.common.response.ResponseResult;
import com.rabbiter.cm.common.utils.ApplicationContextUtils;
import com.rabbiter.cm.domain.SysBill;
import com.rabbiter.cm.domain.SysMovie;
import com.rabbiter.cm.domain.SysSession;
import com.rabbiter.cm.domain.vo.SysBillVo;
import com.rabbiter.cm.service.impl.SysBillServiceImpl;
import com.rabbiter.cm.service.impl.SysMovieServiceImpl;
import com.rabbiter.cm.service.impl.SysSessionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
////这个控制器类是 Spring 框架中 MVC 架构的一部分，主要负责处理与账单相关的 HTTP 请求，协调服务层的不同服务完成业务逻辑。
@RestController
@Api(tags = "订单控制器")
public class SysBillController extends BaseController {

    @Autowired
    private SysBillServiceImpl sysBillService;

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    @Autowired
    private SysMovieServiceImpl sysMovieService;

    @GetMapping("/sysBill")
    @ApiOperation("获取所有订单")
    public ResponseResult findAllBills(SysBill sysBill) {
        startPage();
        // 取消所有超时订单并释放占座资源
        ApplicationContextUtils.getBean("cancelTimeoutBill");
        List<SysBill> data = sysBillService.findAllBills(sysBill);
        return getResult(data);
    }

    @GetMapping("/sysBill/{id}")
    @ApiOperation("根据id获取订单")
    public ResponseResult findBillById(@PathVariable Long id) {
        return getResult(sysBillService.findBillById(id));
    }

    @PostMapping("/sysBill")
    @ApiOperation("增加订单")
    public ResponseResult addBill(@Validated @RequestBody SysBillVo sysBillVo) {
        // 获取当前场次信息
        SysSession curSession = sysSessionService.findOneSession(sysBillVo.getSysBill().getSessionId());
        if (curSession == null) {
            throw new DataNotFoundException("添加订单的场次没找到");
        }
        System.out.println(curSession.getSessionSeats());
        curSession.setSessionSeats(sysBillVo.getSessionSeats());

        int addSallNums = sysBillVo.getSysBill().getSeats().split(",").length;
        curSession.setSallNums(curSession.getSallNums() + addSallNums);
        // 更新场次座位信息
        sysSessionService.updateSession(curSession);

        Object obj = sysBillService.addBill(sysBillVo.getSysBill());
        if (obj instanceof Integer) {
            return getResult((Integer) obj);
        }
        return getResult(obj);
    }

    @PutMapping("/sysBill")
    @ApiOperation("支付订单")
    public ResponseResult pay(@RequestBody SysBill sysBill) {
        int rows = sysBillService.updateBill(sysBill);
        if (rows > 0 && sysBill.getPayState()) {
            //更新场次的座位状态
            SysSession curSession = sysSessionService.findOneSession(sysBill.getSessionId());
            if (curSession == null) {
                throw new DataNotFoundException("支付订单的场次没找到");
            }
            //更新电影票房
            SysMovie curMovie = sysMovieService.findOneMovie(curSession.getMovieId());
            if (curMovie == null) {
                throw new DataNotFoundException("支付订单的电影没找到");
            }
            //订单的座位数
            int seatNum = sysBill.getSeats().split(",").length;
            double price = curSession.getSessionPrice();
            curMovie.setMovieBoxOffice(curMovie.getMovieBoxOffice() + seatNum * price);
            sysMovieService.updateMovie(curMovie);
        }
        return getResult(rows);
    }

    @PutMapping("/sysBill/cancel")
    @ApiOperation("取消订单")
    public ResponseResult cancel(@RequestBody SysBillVo sysBillVo) {
        // 订单取消，更新订单状态
        int rows = sysBillService.updateBill(sysBillVo.getSysBill());
        if (rows > 0 && sysBillVo.getSysBill().getCancelState()) {
            // 订单取消座位不再占用，更新场次的座位状态
            SysSession curSession = sysSessionService.findOneSession(sysBillVo.getSysBill().getSessionId());
            // 取消的订单座位数
            int cancelSallNums = sysBillVo.getSysBill().getSeats().split(",").length;
            curSession.setSallNums(curSession.getSallNums() - cancelSallNums);
            if (curSession == null) {
                throw new DataNotFoundException("添加订单的场次没找到");
            }
            curSession.setSessionSeats(sysBillVo.getSessionSeats());
            sysSessionService.updateSession(curSession);
        }
        return getResult(rows);
    }

    @DeleteMapping("/sysBill/{ids}")
    @ApiOperation("删除订单")
    public ResponseResult deleteBill(@PathVariable Long[] ids) {
        return getResult(sysBillService.deleteBill(ids));
    }

}
