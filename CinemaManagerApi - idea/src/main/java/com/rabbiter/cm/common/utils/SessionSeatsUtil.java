package com.rabbiter.cm.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
/////SessionSeatsUtil 是一个工具类，主要包含一个静态方法 changeSessionSeats，用于处理场次座位信息的更新操作。
// 该方法接收当前场次座位信息 curSessionSeats 和需要修改的座位信息 selectSeats，将 selectSeats 中指定的座位状态修改为未占用（置为 0），
// 并更新 curSessionSeats 的状态，最终将更新后的座位信息以 JSON 字符串的形式返回。
/**
 * @description:
 * @author:
 * @create: 2024-06-02 15:59
 */
public class SessionSeatsUtil {
    /**
     * @param curSessionSeats
     * @param selectSeats
     * @return
     */
    public static String changeSessionSeats(String curSessionSeats, String selectSeats) {
        JSONObject curSessionSeatsJSON = JSONObject.parseObject(curSessionSeats);
        // 超时订单已选座位
        Map<String, Integer> selectedSeatsMap = new LinkedHashMap<>();
        // 获取当前超时订单座位信息
        String[] selectedSeats = selectSeats.split(",");
        for (int i = 0; i < selectedSeats.length; i++) {
            String row = selectedSeats[i].substring(selectedSeats[i].indexOf("\"") + 1, selectedSeats[i].indexOf("排"));
            Integer col = Integer.parseInt(selectedSeats[i].substring(selectedSeats[i].indexOf("排") + 1, selectedSeats[i].indexOf("座")));
            selectedSeatsMap.put(row, col);
        }
        // 显示已选座位坐标
        selectedSeatsMap.forEach((key, value) -> {
            System.out.println("key = " + key + " value=" + value);
        });


        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.putAll(curSessionSeatsJSON);
        valueMap.forEach((key, value) -> System.out.println("\"" + key + "\":" + "   " + value));
        // 取消选座
        selectedSeatsMap.forEach((index, value) -> {
            ((JSONArray) valueMap.get(index)).set(value - 1, 0);
        });
        JSONObject newSessionSeatsJSON = new JSONObject(valueMap);
        return JSONObject.toJSONString(newSessionSeatsJSON);
    }
}
