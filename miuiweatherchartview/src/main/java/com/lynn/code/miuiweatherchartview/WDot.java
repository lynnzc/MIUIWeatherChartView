package com.lynn.code.miuiweatherchartview;

/**
 * Created by Lynn on 7/13/16.
 */

public class WDot {
    private int x;
    private int y;
    private String temp;
    private String time;
    private boolean canDrawDashLine;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean canDrawDashLine() {
        return canDrawDashLine;
    }

    public void setCanDrawDashLine(boolean canDrawDashLine) {
        this.canDrawDashLine = canDrawDashLine;
    }
}
