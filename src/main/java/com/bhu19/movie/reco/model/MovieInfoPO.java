package com.bhu19.movie.reco.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 
 * 
 */

@Data
public class MovieInfoPO implements Serializable {

    private Integer id;

    private Integer year;

    private String name;
}