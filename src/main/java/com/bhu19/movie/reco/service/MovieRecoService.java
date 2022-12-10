package com.bhu19.movie.reco.service;

import com.alibaba.fastjson.JSON;
import com.bhu19.movie.reco.adapter.MovieScore;
import com.bhu19.movie.reco.adapter.PredictRes;
import com.bhu19.movie.reco.adapter.TFServingAdapter;
import com.bhu19.movie.reco.dao.MovieInfoDAO;
import com.bhu19.movie.reco.dao.MovieRateDAO;
import com.bhu19.movie.reco.model.MovieInfoPO;
import com.bhu19.movie.reco.model.MovieInfoVO;
import com.bhu19.movie.reco.model.MovieRatePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/11/26 5:23 下午
 * @description:
 */

@Slf4j
@Service
public class MovieRecoService {

    @Resource
    private MovieInfoDAO movieInfoDAO;
    @Resource
    private MovieRateDAO movieRateDAO;
    @Resource
    private TFServingAdapter tfServingAdapter;

    public List<MovieInfoPO> history(Integer custId) {
        Assert.notNull(custId, "custId不能为空");
        log.info("[history] custId={}", custId);
        List<MovieRatePO> rates = movieRateDAO.selectByCustId(custId);
        Assert.isTrue(!CollectionUtils.isEmpty(rates), "movieRateDAO.selectByCustId返回为空");
        List<Integer> ids = rates.stream().map(MovieRatePO::getMovieId).distinct().collect(Collectors.toList());
        List<MovieInfoPO> movies = movieInfoDAO.selectByIdBatch(ids);
        log.info("[history] final movies={}", JSON.toJSONString(movies));
        return movies;
    }

    public List<MovieInfoVO> recall(Integer custId) {
        Assert.notNull(custId, "custId不能为空");
        log.info("[recall] custId={}", custId);
        List<MovieRatePO> rates = movieRateDAO.selectByCustId(custId);
        Assert.isTrue(!CollectionUtils.isEmpty(rates), "movieRateDAO.selectByCustId返回为空");
        PredictRes recallRes = tfServingAdapter.recall(rates);
        Map<Integer, MovieScore> scoreMap = recallRes.getSorted().stream()
                .collect(Collectors.toMap(MovieScore::getMovieId, Function.identity()));
        List<Integer> sortIds = recallRes.getSorted().stream()
                .map(MovieScore::getMovieId).collect(Collectors.toList());
        List<MovieInfoPO> movies = movieInfoDAO.selectByIdBatch(sortIds);
        List<MovieInfoVO> res = movies.stream()
                .map(m -> MovieInfoVO.from(m, scoreMap.get(m.getId()))).collect(Collectors.toList());
        res.sort(Comparator.comparingDouble(MovieInfoVO::getFloatScore).reversed());
        log.info("[recall] final movies={}", JSON.toJSONString(res));
        return res;
    }

    public List<MovieInfoVO> sort(Integer custId) {
        Assert.notNull(custId, "custId不能为空");
        log.info("[sort] custId={}", custId);
        List<MovieRatePO> rates = movieRateDAO.selectByCustId(custId);
        Assert.isTrue(!CollectionUtils.isEmpty(rates), "movieRateDAO.selectByCustId返回为空");
        PredictRes recallRes = tfServingAdapter.recall(rates);
        PredictRes sortRes = tfServingAdapter.sort(recallRes.getPredictions());
        Map<Integer, MovieScore> scoreMap = sortRes.getSorted().stream()
                .collect(Collectors.toMap(MovieScore::getMovieId, Function.identity()));
        List<Integer> sortIds = sortRes.getSorted().stream()
                .map(MovieScore::getMovieId).collect(Collectors.toList());
        List<MovieInfoPO> movies = movieInfoDAO.selectByIdBatch(sortIds);
        List<MovieInfoVO> res = movies.stream()
                .map(m -> MovieInfoVO.from(m, scoreMap.get(m.getId()))).collect(Collectors.toList());
        res.sort(Comparator.comparingDouble(MovieInfoVO::getFloatScore).reversed());
        log.info("[sort] final movies={}", JSON.toJSONString(res));
        return res;
    }
}
