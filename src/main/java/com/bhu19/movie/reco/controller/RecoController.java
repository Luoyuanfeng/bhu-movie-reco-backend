package com.bhu19.movie.reco.controller;

import com.bhu19.movie.reco.model.MovieInfoPO;
import com.bhu19.movie.reco.service.MovieRecoService;
import com.bhu19.movie.reco.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/11/26 5:22 下午
 * @description:
 */

@Slf4j
@RestController
@RequestMapping("/api/reco")
public class RecoController {

    private static final Integer BIZ_ERR_CODE = 100;
    private static final Integer SYS_ERR_CODE = 100;

    @Resource
    private MovieRecoService movieRecoService;

    @GetMapping("/predict")
    public HttpResp<?> predict(@RequestParam("custId") Integer custId) {
        try {
            List<MovieInfoPO> movies = movieRecoService.predict(custId);
            return HttpResp.success(movies);
        } catch (IllegalArgumentException e) {
            log.error("[predict] biz error custId={} e=", custId, e);
            return HttpResp.fail(BIZ_ERR_CODE, ExceptionUtils.parseException(e));
        } catch (Exception e) {
            log.error("[predict] sys error custId={} e=", custId, e);
            return HttpResp.fail(SYS_ERR_CODE, ExceptionUtils.parseException(e));
        }
    }

    @GetMapping("/history")
    public HttpResp<?> history(@RequestParam("custId") Integer custId) {
        try {
            List<MovieInfoPO> movies = movieRecoService.history(custId);
            return HttpResp.success(movies);
        } catch (IllegalArgumentException e) {
            log.error("[history] biz error custId={} e=", custId, e);
            return HttpResp.fail(BIZ_ERR_CODE, ExceptionUtils.parseException(e));
        } catch (Exception e) {
            log.error("[history] sys error custId={} e=", custId, e);
            return HttpResp.fail(SYS_ERR_CODE, ExceptionUtils.parseException(e));
        }
    }
}
