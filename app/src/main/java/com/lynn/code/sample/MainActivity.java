package com.lynn.code.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lynn.code.miuiweatherchartview.WNode;
import com.lynn.code.miuiweatherchartview.Weather;
import com.lynn.code.miuiweatherchartview.WeatherChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * demo activity
 */
public class MainActivity extends AppCompatActivity {
    private WeatherChartView mChartView;
    private List<WNode> mWeathers;
    private Executor mTestExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChartView = (WeatherChartView) findViewById(R.id.weather_chart);

        mTestExecutor = Executors.newSingleThreadExecutor();

        mTestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //fulfill the test data
                setTestDatas();

                mChartView.setWeathers(mWeathers);
            }
        });
    }

    private void setTestDatas() {
        mWeathers = new ArrayList<>();
        Random random = new Random();
        int w = 0;
        for (int i = 0; i < 24; i++) {
            if (i % 3 == 0) {
                w = random.nextInt(1000) % 3;
            }
            WNode wNode = new WNode();
            //if i == size - 1, the last node's weather status is meaningless
            wNode.setWeather(getWeather(w));
            wNode.setTime((i + 1) + ":00");
            wNode.setTemperature(i % 6 + 28);
            mWeathers.add(wNode);
        }
    }

    @Weather
    private int getWeather(int n) {
        switch (n) {
            case 0:
                return Weather.RAINY;
            case 1:
                return Weather.CLOUDY;
            case 2:
            default:
                return Weather.SUNNY;
        }
    }
}
