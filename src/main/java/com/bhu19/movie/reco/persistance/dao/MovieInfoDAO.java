package com.bhu19.movie.reco.persistance.dao;

import com.bhu19.movie.reco.model.po.MovieInfoPO;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieInfoDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MovieInfoPO record);

    int insertSelective(MovieInfoPO record);

    MovieInfoPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieInfoPO record);

    int updateByPrimaryKey(MovieInfoPO record);
}