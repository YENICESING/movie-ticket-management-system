package com.rabbiter.cm.common.page;

import com.rabbiter.cm.common.utils.ServletUtil;
import com.rabbiter.cm.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
//////用于构建分页对象，从 HttpServletRequest 中获取分页相关的参数，并将其设置到 Page 对象中。
/**
 * 构建页对象，通过ServletUtil减少Controller方法上的请求参数
 */
public class PageBuilder {

    //当前页码
    public static final String PAGE_NUM = "pageNum";

    //页大小
    public static final String PAGE_SIZE = "pageSize";

    //总记录数
    public static final String TOTAL = "total";

    //按列排序
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    //升序还是降序
    public static final String IS_ASC = "isAsc";

    public static Page buildPage() {
        Page page = new Page();
        HttpServletRequest request = ServletUtil.getRequest();

        String pageNum = request.getParameter(PAGE_NUM);
        if (StringUtil.isNotEmpty(pageNum)) {
            page.setPageNum(Integer.parseInt(pageNum));
        }

        String pageSize = request.getParameter(PAGE_SIZE);
        if (StringUtil.isNotEmpty(pageSize)) {
            page.setPageSize(Integer.parseInt(pageSize));
        }

        page.setIsAsc(request.getParameter(IS_ASC));
        page.setOrderByColumn(StringUtil.toUnderScoreCase(request.getParameter(ORDER_BY_COLUMN)));
        return page;
    }

}
