package com.bhu19.movie.reco.model.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 * 电影信息表
 */
public class MovieInfoPO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 电影名称
     */
    private String movieName;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}