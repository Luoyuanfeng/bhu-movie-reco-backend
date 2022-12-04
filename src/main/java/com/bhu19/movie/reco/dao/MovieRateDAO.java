package com.bhu19.movie.reco.dao;

import com.bhu19.movie.reco.model.MovieRatePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRateDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(MovieRatePO record);

    int insertSelective(MovieRatePO record);

    MovieRatePO selectByPrimaryKey(Integer id);

    List<MovieRatePO> selectByCustId(@Param("custId") Integer custId);

    int updateByPrimaryKeySelective(MovieRatePO record);

    int updateByPrimaryKey(MovieRatePO record);
}