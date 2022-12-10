package com.bhu19.movie.reco.model;

import com.bhu19.movie.reco.adapter.MovieScore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 
 * 
 */

@Data
public class MovieInfoVO implements Serializable {

    private Integer id;

    private Integer year;

    private String name;

    private String score;

    private Float floatScore;

    public static MovieInfoVO from(MovieInfoPO po, MovieScore ms) {
        MovieInfoVO vo = new MovieInfoVO();
        vo.setId(po.getId());
        vo.setYear(po.getYear());
        vo.setName(po.getName());
        vo.setScore(String.valueOf(ms.getScore()));
        vo.setFloatScore(ms.getScore());
        return vo;
    }
}