package com.lynn.code.miuiweatherchartview;

/**
 * data
 * Created by Lynn on 7/11/16.
 */

public class WNode {
    //温度 摄氏
    private int temperature;
    //时间
    private String time;
    //天气
    @Weather
    private int weather;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Weather
    public int getWeather() {
        return weather;
    }

    public void setWeather(@Weather int weather) {
        this.weather = weather;
    }
}
