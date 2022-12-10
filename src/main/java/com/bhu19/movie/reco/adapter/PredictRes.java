package com.bhu19.movie.reco.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/12/4 4:52 下午
 * @description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictRes {

    private List<Float> predictions;

    private List<MovieScore> sorted;
}
