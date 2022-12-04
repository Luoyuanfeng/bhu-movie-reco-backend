package com.bhu19.movie.reco.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 * 
 */

@Data
public class MovieRatePO implements Serializable {

    private Integer id;

    private Integer custId;

    private Float rating;

    private Date date;

    private Integer movieId;

    private Float movieCount;

    private Float userRatingMean;

    private Float userCount;

    private Float movieRatingMean;

    private Integer year;

    private Integer month;

    private Integer day;

    private Float movieYear;

    private Float yearGap;

    private String watchedMovieLists;

    private String ratingLists;
}