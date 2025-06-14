package com.rabbiter.cm.domain;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
////SysMovie 类是一个 Java 实体类，用于表示电影的信息。它实现了 Serializable 接口，可序列化，方便对象在网络传输或存储到文件、数据库时进行序列化操作。
// 该类包含了电影的各种属性，如电影 ID、名称、时长、海报、地区、上映日期、总票房、简介、图集和类别列表等，
// 并提供了属性的访问器（getter）和修改器（setter）方法，以及 equals、hashCode 和 toString 方法。

public class SysMovie implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movieId;

    //电影名称
    @NotBlank(message = "电影名称不能为空")
    private String movieName;

    //电影时长
    private Integer movieLength;

    //电影海报
    private String moviePoster;

    private String movieArea;

    //上映日期
    private Date releaseDate;

    //电影总票房
    private Double movieBoxOffice;

    //电影简介
    private String movieIntroduction;

    //电影图集
    private String moviePictures;

    //电影的类别
    private List<SysMovieCategory> movieCategoryList;

    public SysMovie() {
    }

    public SysMovie(Long movieId, String movieName, Integer movieLength, String moviePoster, String movieArea, Date releaseDate, Double movieBoxOffice, String movieIntroduction, String moviePictures, List<SysMovieCategory> movieCategoryList) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieLength = movieLength;
        this.moviePoster = moviePoster;
        this.movieArea = movieArea;
        this.releaseDate = releaseDate;
        this.movieBoxOffice = movieBoxOffice;
        this.movieIntroduction = movieIntroduction;
        this.moviePictures = moviePictures;
        this.movieCategoryList = movieCategoryList;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(Integer movieLength) {
        this.movieLength = movieLength;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieArea() {
        return movieArea;
    }

    public void setMovieArea(String movieArea) {
        this.movieArea = movieArea;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getMovieBoxOffice() {
        return movieBoxOffice;
    }

    public void setMovieBoxOffice(Double movieBoxOffice) {
        this.movieBoxOffice = movieBoxOffice;
    }

    public String getMovieIntroduction() {
        return movieIntroduction;
    }

    public void setMovieIntroduction(String movieIntroduction) {
        this.movieIntroduction = movieIntroduction;
    }

    public String getMoviePictures() {
        return moviePictures;
    }

    public void setMoviePictures(String moviePictures) {
        this.moviePictures = moviePictures;
    }

    public List<SysMovieCategory> getMovieCategoryList() {
        return movieCategoryList;
    }

    public void setMovieCategoryList(List<SysMovieCategory> movieCategoryList) {
        this.movieCategoryList = movieCategoryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysMovie sysMovie = (SysMovie) o;
        return Objects.equals(movieId, sysMovie.movieId) && Objects.equals(movieName, sysMovie.movieName) && Objects.equals(movieLength, sysMovie.movieLength) && Objects.equals(moviePoster, sysMovie.moviePoster) && Objects.equals(movieArea, sysMovie.movieArea) && Objects.equals(releaseDate, sysMovie.releaseDate) && Objects.equals(movieBoxOffice, sysMovie.movieBoxOffice) && Objects.equals(movieIntroduction, sysMovie.movieIntroduction) && Objects.equals(moviePictures, sysMovie.moviePictures) && Objects.equals(movieCategoryList, sysMovie.movieCategoryList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieName, movieLength, moviePoster, movieArea, releaseDate, movieBoxOffice, movieIntroduction, moviePictures, movieCategoryList);
    }

    @Override
    public String toString() {
        return "SysMovie{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", movieLength=" + movieLength +
                ", moviePoster='" + moviePoster + '\'' +
                ", movieArea='" + movieArea + '\'' +
                ", releaseDate=" + releaseDate +
                ", movieBoxOffice=" + movieBoxOffice +
                ", movieIntroduction='" + movieIntroduction + '\'' +
                ", moviePictures='" + moviePictures + '\'' +
                ", movieCategoryList=" + movieCategoryList +
                '}';
    }
}
