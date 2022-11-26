package com.bhu19.movie.reco.controller;

import com.bhu19.movie.reco.service.MovieRecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/11/26 5:22 下午
 * @description:
 */

@Slf4j
@RestController
@RequestMapping("/api/reco")
public class RecoController {

    @Resource
    private MovieRecoService movieRecoService;
}
