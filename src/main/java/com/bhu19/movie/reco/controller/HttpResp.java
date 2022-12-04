package com.bhu19.movie.reco.controller;

import lombok.Data;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/12/4 5:28 下午
 * @description:
 */

@Data
public class HttpResp<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> HttpResp<T> success(T data) {
        HttpResp<T> resp = new HttpResp<>();
        resp.setCode(0);
        resp.setMessage("");
        resp.setData(data);
        return resp;
    }

    public static HttpResp<?> fail(Integer code, String message) {
        HttpResp<?> resp = new HttpResp<>();
        resp.setCode(code);
        resp.setMessage(message);
        resp.setData(null);
        return resp;
    }
}
