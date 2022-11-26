package com.bhu19.movie.reco.service;

import com.bhu19.movie.reco.persistance.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/11/26 5:23 下午
 * @description:
 */

@Slf4j
@Service
public class MovieRecoService {

    @Resource
    private MovieRepository movieRepository;
}
