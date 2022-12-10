package com.bhu19.movie.reco.adapter;

import com.alibaba.fastjson.JSON;
import com.bhu19.movie.reco.model.MovieRatePO;
import com.bhu19.movie.reco.utils.Crawler;
import com.bhu19.movie.reco.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PredictRes recall(List<MovieRatePO> rates) {
        // for mock
        List<Float> mockRes = RandomUtils.randomList(15000);
        PredictRes res = new PredictRes();
        res.setPredictions(mockRes);
        res.setSorted(MovieScore.fromListAndSort(res.getPredictions(), 1000));
        return res;

//        log.info("[recall] rates={}", JSON.toJSONString(rates));
//        Crawler crawler = Crawler.of(RECALL_URL, false);
//        PredictReq<MovieRatePO> req = new PredictReq<>(rates);
//        String resp = crawler.doPost(JSON.toJSONString(req));
//        log.info("[recall] resp={}", resp);
//        PredictRes res = JSON.parseObject(resp, PredictRes.class);
//        res.setSorted(MovieScore.fromListAndSort(res.getPredictions(), 1000));
//        return res;
    }

    public PredictRes sort(List<Float> scores) {
        // for mock
        PredictRes res = new PredictRes();
        res.setPredictions(scores);
        res.setSorted(MovieScore.fromListAndSort(res.getPredictions(), 30));
        return res;

//        log.info("[sort] scores={}", JSON.toJSONString(scores));
//        Crawler crawler = Crawler.of(SORT_URL, false);
//        PredictReq<Float> req = new PredictReq<>(scores);
//        String resp = crawler.doPost(JSON.toJSONString(req));
//        log.info("[sort] resp={}", resp);
//        PredictRes res = JSON.parseObject(resp, PredictRes.class);
//        res.setSorted(MovieScore.fromListAndSort(res.getPredictions(), 30));
//        return res;
    }
}
