package com.rabbiter.cm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
////该类在实际应用中可能会作为数据传输对象（DTO），在服务层和表现层之间传递影院信息，或者作为实体类的一部分，存储和处理影院信息。
////用于存储和表示影院的相关信息，包含影院的基本信息，如 cinemaId（影院唯一标识符）、cinemaName（影院名称）、
// hallCategoryList（厅类别列表）、cinemaPicture（影院图片）、cinemaAddress（影院地址）、cinemaPhone（影院电话）、
// workStartTime（工作开始时间）、workEndTime（工作结束时间）以及 sysMovieList（当前影院上映的电影列表）。
// // 这些信息可以用于描述一个影院的详细属性，为系统中涉及影院的业务逻辑提供数据支持。
public class SysCinema implements Serializable {
    private final static Long serialVersionUID = 1L;

    private Long cinemaId;

    @NotBlank(message = "影院名称不能为空")
    private String cinemaName;

    private String hallCategoryList;

    private String cinemaPicture;

    private String cinemaAddress;

    private String cinemaPhone;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String workStartTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String workEndTime;

    //当前影院上映的所有电影，规则：上映的电影指包括今天在内，未来5天有安排目标影片的场次
    private List<SysMovie> sysMovieList;

    public SysCinema() {
    }

    public SysCinema(Long cinemaId, String cinemaName, String hallCategoryList, String cinemaPicture, String cinemaAddress, String cinemaPhone, String workStartTime, String workEndTime, List<SysMovie> sysMovieList) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.hallCategoryList = hallCategoryList;
        this.cinemaPicture = cinemaPicture;
        this.cinemaAddress = cinemaAddress;
        this.cinemaPhone = cinemaPhone;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.sysMovieList = sysMovieList;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getHallCategoryList() {
        return hallCategoryList;
    }

    public void setHallCategoryList(String hallCategoryList) {
        this.hallCategoryList = hallCategoryList;
    }

    public String getCinemaPicture() {
        return cinemaPicture;
    }

    public void setCinemaPicture(String cinemaPicture) {
        this.cinemaPicture = cinemaPicture;
    }

    public String getCinemaAddress() {
        return cinemaAddress;
    }

    public void setCinemaAddress(String cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public void setCinemaPhone(String cinemaPhone) {
        this.cinemaPhone = cinemaPhone;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public List<SysMovie> getSysMovieList() {
        return sysMovieList;
    }

    public void setSysMovieList(List<SysMovie> sysMovieList) {
        this.sysMovieList = sysMovieList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysCinema sysCinema = (SysCinema) o;
        return Objects.equals(cinemaId, sysCinema.cinemaId) && Objects.equals(cinemaName, sysCinema.cinemaName) && Objects.equals(hallCategoryList, sysCinema.hallCategoryList) && Objects.equals(cinemaPicture, sysCinema.cinemaPicture) && Objects.equals(cinemaAddress, sysCinema.cinemaAddress) && Objects.equals(cinemaPhone, sysCinema.cinemaPhone) && Objects.equals(workStartTime, sysCinema.workStartTime) && Objects.equals(workEndTime, sysCinema.workEndTime) && Objects.equals(sysMovieList, sysCinema.sysMovieList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cinemaId, cinemaName, hallCategoryList, cinemaPicture, cinemaAddress, cinemaPhone, workStartTime, workEndTime, sysMovieList);
    }

    @Override
    public String toString() {
        return "SysCinema{" +
                "cinemaId=" + cinemaId +
                ", cinemaName='" + cinemaName + '\'' +
                ", hallCategoryList='" + hallCategoryList + '\'' +
                ", cinemaPicture='" + cinemaPicture + '\'' +
                ", cinemaAddress='" + cinemaAddress + '\'' +
                ", cinemaPhone='" + cinemaPhone + '\'' +
                ", workStartTime='" + workStartTime + '\'' +
                ", workEndTime='" + workEndTime + '\'' +
                ", sysMovieList=" + sysMovieList +
                '}';
    }
}
