package com.rabbiter.cm.controller;

import com.rabbiter.cm.common.constant.MovieRankingList;
import com.rabbiter.cm.common.response.ResponseResult;
import com.rabbiter.cm.domain.SysMovie;
import com.rabbiter.cm.domain.vo.SysMovieVo;
import com.rabbiter.cm.service.impl.SysMovieServiceImpl;
import com.rabbiter.cm.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
////SysMovieController 类在整个系统中起到了连接前端 HTTP 请求和后端服务层的桥梁作用，根据不同的请求类型和路径，调用相应的服务层方法完成电影信息的查询、
// 添加、更新、删除以及电影榜单的查询操作，将操作结果以统一的 ResponseResult 格式返回，便于前端处理和显示，同时利用了 Spring 框架的特性，
// 保证了代码的简洁性和可维护性。

@RestController
public class SysMovieController extends BaseController {

    @Autowired
    private SysMovieServiceImpl sysMovieService;

    @GetMapping("/sysMovie/find")
    public ResponseResult findAllMovies(SysMovieVo sysMovieVo) {
        startPage();
        List<SysMovie> data = sysMovieService.findAllMovies(sysMovieVo);
        return getResult(data);
    }

    @GetMapping("/sysMovie/find/{id}")
    public ResponseResult findMovieById(@PathVariable Long id) {
        return getResult(sysMovieService.findMovieById(id));
    }

    @PostMapping("/sysMovie")
    public ResponseResult addMovie(@Validated @RequestBody SysMovie sysMovie) {
        return getResult(sysMovieService.addMovie(sysMovie));
    }

    @PutMapping("/sysMovie")
    public ResponseResult updateMovie(@Validated @RequestBody SysMovie sysMovie) {
        return getResult(sysMovieService.updateMovie(sysMovie));
    }

    @DeleteMapping("/sysMovie/{ids}")
    public ResponseResult deleteMovie(@PathVariable Long[] ids) {
        return getResult(sysMovieService.deleteMovie(ids));
    }

    @GetMapping("/sysMovie/find/rankingList/{listId}")
    public ResponseResult findRankingList(@PathVariable Integer listId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (listId <= 0 || listId > 4) {
            //暂时只支持4种榜单
            return ResponseResult.error("抱歉，暂时只支持3种榜单，id为[1,3]");
        }
        Method getList = sysMovieService.getClass().getMethod(MovieRankingList.listNames[listId - 1]);
        startPage();
        List<SysMovie> data = (List<SysMovie>) getList.invoke(sysMovieService);
        return getResult(data);
    }

}
