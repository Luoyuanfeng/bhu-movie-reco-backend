package com.bhu19.movie.reco.persistance.repository;

import com.bhu19.movie.reco.persistance.dao.MovieInfoDAO;
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
public class MovieRepository {

    @Resource
    private MovieInfoDAO movieInfoDAO;
}
