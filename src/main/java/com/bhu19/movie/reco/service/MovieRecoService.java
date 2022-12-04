package com.bhu19.movie.reco.service;

import com.alibaba.fastjson.JSON;
import com.bhu19.movie.reco.adapter.TFServingAdapter;
import com.bhu19.movie.reco.dao.MovieInfoDAO;
import com.bhu19.movie.reco.dao.MovieRateDAO;
import com.bhu19.movie.reco.model.MovieInfoPO;
import com.bhu19.movie.reco.model.MovieRatePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<MovieInfoPO> predict(Integer custId) {
        Assert.notNull(custId, "custId不能为空");
        log.info("[predict] custId={}", custId);
        List<MovieRatePO> rates = movieRateDAO.selectByCustId(custId);
        Assert.isTrue(!CollectionUtils.isEmpty(rates), "movieRateDAO.selectByCustId返回为空");
        List<Integer> recallIds = tfServingAdapter.recall(rates);
        List<Integer> sortIds = tfServingAdapter.sort(recallIds);
        Assert.isTrue(!CollectionUtils.isEmpty(sortIds), "返回的排序id为空");
        List<MovieInfoPO> movies = movieInfoDAO.selectByIdBatch(sortIds);
        log.info("[predict] final movies={}", JSON.toJSONString(movies));
        return movies;
    }
}
