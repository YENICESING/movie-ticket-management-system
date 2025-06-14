package com.rabbiter.cm.domain.vo;

import com.rabbiter.cm.domain.SysBill;

import java.io.Serializable;
///SysBillVo 类是一个 Java 实体类，实现了 Serializable 接口，表明该类的对象可以被序列化，适用于网络传输或存储操作。
// 该类主要作为视图对象（VO），用于接收前端发送的增加订单的数据，包含了订单信息（SysBill 类型）和更新后场次的座位信息（String 类型），
// 并提供了相应的属性访问器（getter）和修改器（setter）方法，同时重写了 toString 方法，方便对象信息的输出。
/**
 * 接收前端增加订单的数据
 */
public class SysBillVo implements Serializable {

    //订单信息
    private SysBill sysBill;

    //若成功更新后场次的座位信息
    private String sessionSeats;

    public SysBillVo() {
    }

    public SysBillVo(SysBill sysBill, String sessionSeats) {
        this.sysBill = sysBill;
        this.sessionSeats = sessionSeats;
    }

    public SysBill getSysBill() {
        return sysBill;
    }

    public void setSysBill(SysBill sysBill) {
        this.sysBill = sysBill;
    }

    public String getSessionSeats() {
        return sessionSeats;
    }

    public void setSessionSeats(String sessionSeats) {
        this.sessionSeats = sessionSeats;
    }

    @Override
    public String toString() {
        return "SysBillVo{" +
                "sysBill=" + sysBill +
                ", sessionSeats='" + sessionSeats + '\'' +
                '}';
    }
}
