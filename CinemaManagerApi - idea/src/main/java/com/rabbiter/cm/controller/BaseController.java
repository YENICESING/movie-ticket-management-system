package com.rabbiter.cm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbiter.cm.common.page.Page;
import com.rabbiter.cm.common.page.PageBuilder;
import com.rabbiter.cm.common.response.ResponseResult;

import java.util.List;

import static com.rabbiter.cm.common.page.PageBuilder.*;
/////BaseController 类为控制器提供了分页操作的启动方法和不同情况下构建响应结果的方法，它结合了 PageBuilder 类和 ResponseResult 类，
// 将分页和响应结果处理的功能集成在一起，
/**
 * 抽取重复功能为基类
 */
public class BaseController {

    /**
     * 开启分页
     */
    public void startPage() {
        Page page = PageBuilder.buildPage();
        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize, page.getOrderByColumn());
        }
    }

    /**
     * 根据修改行数返回响应消息
     * @param rows
     * @return
     */
    public ResponseResult getResult(int rows) {
        return rows == 0 ? ResponseResult.error() : ResponseResult.success();
    }

    /**
     * 分页响应消息
     * @param data
     * @return
     */
    public ResponseResult getResult(List<?> data) {
        PageInfo pageInfo = new PageInfo(data);
        ResponseResult responseResult = ResponseResult.success(data);
        responseResult.put(PAGE_NUM, pageInfo.getPageNum());
        responseResult.put(PAGE_SIZE, pageInfo.getPageSize());
        responseResult.put(TOTAL, pageInfo.getTotal());
        return responseResult;
    }

    /**
     * 对象类型响应消息
     * @param data
     * @return
     */
    public ResponseResult getResult(Object data) {
        return ResponseResult.success(data);
    }

}
