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

    public static MovieInfoVO from(MovieInfoPO po, MovieScore ms) {
        MovieInfoVO vo = new MovieInfoVO();
        vo.setId(po.getId());
        vo.setYear(po.getYear());
        vo.setName(po.getName());
        vo.setScore(String.valueOf(ms.getScore()));
        return vo;
    }
}