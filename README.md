# MIUIWeatherChartView
    
  [MIUIWeatherChartView](https://github.com/lynnzc/MIUIWeatherChartView) 
  is a high imitation of the chart view from MIUI Weather app.  
  ![preview](https://github.com/lynnzc/MIUIWeatherChartView/blob/master/preview/miuidemo.gif)

# Usage

  add the view to your xml layout  
    
	<com.lynn.code.miuiweatherchartview.WeatherChartView
	        android:id="@+id/weather_chart"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        app:fillColor="#63c3d6"
	        app:textColor="#000000" />

  initializing the view and setting values
    
	WeatherChartView chartView = (WeatherChartView) findViewById(R.id.weather_chart);

	List<WNode> weathers = new ArrayList<>();

	chartView.setWeathers(weathers);

# License

	Copyright (C) 2016, Lynn

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
