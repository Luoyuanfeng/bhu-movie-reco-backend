package com.bhu19.movie.reco.adapter;

import com.alibaba.fastjson.JSON;
import com.bhu19.movie.reco.model.MovieRatePO;
import com.bhu19.movie.reco.utils.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/11/26 5:22 下午
 * @description:
 */

@Slf4j
@Service
public class TFServingAdapter {

    /**
     * 接口文档：https://www.tensorflow.org/tfx/serving/api_rest#predict_api
     * API：POST http://host:port/v1/models/${MODEL_NAME}[/versions/${VERSION}|/labels/${LABEL}]:predict
     * 参数：{
     *   // (Optional) Serving signature to use.
     *   // If unspecifed default serving signature is used.
     *   "signature_name": <string>,
     *
     *   // Input Tensors in row ("instances") or columnar ("inputs") format.
     *   // A request can have either of them but NOT both.
     *   "instances": <value>|<(nested)list>|<list-of-objects>
     *   "inputs": <value>|<(nested)list>|<object>
     * }
     * 返回值：
     * {
     *   "predictions": <value>|<(nested)list>|<list-of-objects>
     * }
     */

    private static final String RECALL_URL = "http://127.0.0.1:port/v1/models/${MODEL_NAME}:predict";
    private static final String SORT_URL = "http://127.0.0.1:port/v1/models/${MODEL_NAME}:predict";

    public List<Integer> recall(List<MovieRatePO> rates) {
        log.info("[recall] rates={}", JSON.toJSONString(rates));
        Crawler crawler = Crawler.of(RECALL_URL, false);
        PredictReq<MovieRatePO> req = new PredictReq<>(rates);
        String resp = crawler.doPost(JSON.toJSONString(req));
        log.info("[recall] resp={}", resp);
        PredictRes res = JSON.parseObject(resp, PredictRes.class);
        return Optional.ofNullable(res).map(PredictRes::getPredictions).orElse(new ArrayList<>());
    }

    public List<Integer> sort(List<Integer> ids) {
        log.info("[sort] ids={}", JSON.toJSONString(ids));
        Crawler crawler = Crawler.of(SORT_URL, false);
        PredictReq<Integer> req = new PredictReq<>(ids);
        String resp = crawler.doPost(JSON.toJSONString(req));
        log.info("[sort] resp={}", resp);
        PredictRes res = JSON.parseObject(resp, PredictRes.class);
        return Optional.ofNullable(res).map(PredictRes::getPredictions).orElse(new ArrayList<>());
    }
}
