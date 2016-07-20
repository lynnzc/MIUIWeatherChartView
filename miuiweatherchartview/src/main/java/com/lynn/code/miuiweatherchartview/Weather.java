package com.lynn.code.miuiweatherchartview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.lynn.code.miuiweatherchartview.Weather.CLOUDY;
import static com.lynn.code.miuiweatherchartview.Weather.RAINY;
import static com.lynn.code.miuiweatherchartview.Weather.SUNNY;

/**
 * Weather representation
 * Created by Lynn on 7/11/16.
 */

@IntDef(value = {RAINY, SUNNY, CLOUDY})
@Retention(RetentionPolicy.SOURCE)
public @interface Weather {
    //雨天
    int RAINY = 0;
    //晴天
    int SUNNY = 1;
    //多云
    int CLOUDY = 2;
}
