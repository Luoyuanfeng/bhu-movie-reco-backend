package com.bhu19.movie.reco.dao;

import com.bhu19.movie.reco.model.MovieInfoPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieInfoDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(MovieInfoPO record);

    int insertSelective(MovieInfoPO record);

    MovieInfoPO selectByPrimaryKey(Integer id);

    List<MovieInfoPO> selectByIdBatch(@Param("ids") List<Integer> ids);

    int updateByPrimaryKeySelective(MovieInfoPO record);

    int updateByPrimaryKey(MovieInfoPO record);
}