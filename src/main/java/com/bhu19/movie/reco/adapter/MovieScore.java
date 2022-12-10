package com.bhu19.movie.reco.adapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/12/10 5:27 下午
 * @description:
 */

@Data
public class MovieScore {

    private Integer movieId;
    private Float score;

    public static List<MovieScore> fromListAndSort(List<Float> scores, Integer topK) {
        List<MovieScore> tmp = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            MovieScore ms = new MovieScore();
            ms.setMovieId(i);
            ms.setScore(scores.get(i));
            tmp.add(ms);
        }
        tmp.sort(Comparator.comparingDouble(MovieScore::getScore).reversed());
        List<MovieScore> res = new ArrayList<>();
        for (int i = 0; i < tmp.size(); i++) {
            if (i >= topK) {
                break;
            }
            res.add(tmp.get(i));
        }
        return res;
    }
}
