package com.bhu19.movie.reco.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/12/10 5:10 下午
 * @description:
 */
public class RandomUtils {

    private RandomUtils() {

    }

    public static Float random() {
        double random = Math.random();
        random *= 10;
        random /= 2;
        BigDecimal bd = new BigDecimal(random);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static List<Float> randomList(int n) {
        List<Float> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(random());
        }
        return res;
    }
}
