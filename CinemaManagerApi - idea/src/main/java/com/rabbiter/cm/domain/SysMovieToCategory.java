package com.rabbiter.cm.domain;

import java.io.Serializable;
import java.util.Objects;
////该类的设计是为了存储电影与电影类别之间的多对多联系。在数据库设计中，当存在多对多关系时，通常会使用一个关联表来存储这种关系，
// 而这个类可能就是用于表示该关联表中的记录。例如，一个电影可以属于多个类别，一个类别也可以包含多个电影，通过 SysMovieToCategory 类可以表示这种关联关系。
/**
 * 存储电影与电影类别的多对多联系
 */
public class SysMovieToCategory implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long movieId;

    private Long movieCategoryId;


    public SysMovieToCategory() {
    }

    public SysMovieToCategory(Long movieId, Long movieCategoryId) {
        this.movieId = movieId;
        this.movieCategoryId = movieCategoryId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getMovieCategoryId() {
        return movieCategoryId;
    }

    public void setMovieCategoryId(Long movieCategoryId) {
        this.movieCategoryId = movieCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysMovieToCategory that = (SysMovieToCategory) o;
        return Objects.equals(movieId, that.movieId) && Objects.equals(movieCategoryId, that.movieCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieCategoryId);
    }

    @Override
    public String toString() {
        return "SysMovieToCategory{" +
                "movieId=" + movieId +
                ", movieCategoryId=" + movieCategoryId +
                '}';
    }
}
