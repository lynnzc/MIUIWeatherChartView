package com.lynn.code.miuiweatherchartview;

import java.util.List;

/**
 * Created by Lynn on 7/14/16.
 */

public class WHint {
    private float curX;
    private List<WDot> hintDots;
    @Weather
    private int weather;

    public float getCurX() {
        return curX;
    }

    public void setCurX(float curX) {
        this.curX = curX;
    }

    public List<WDot> getHintDots() {
        return hintDots;
    }

    public void setHintDots(List<WDot> hintDots) {
        this.hintDots = hintDots;
    }

    @Weather
    public int getWeather() {
        return weather;
    }

    public void setWeather(@Weather int weather) {
        this.weather = weather;
    }
}
