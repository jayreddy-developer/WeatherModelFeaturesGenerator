#WeatherModelFeaturesGenerator
<p>This is weather model features generation application.<br>
At the moment geography data aggregated consists of : LATITUDE, LONGITUDE, ELEVATION. <br>
And weather data aggregated consists of: TEMPERATURE, PRESSURE, HUMIDITY <br>
</p>
###The command to run the application
>sbt "test-only com.jpreddy.tests.featuresgen.weather.TestWeatherModelFeaturesGeneratorApp"

###Input to the applications is at this location:
>src/main/resources/input/WeatherStationNames_IATA.txt

####Sample input
######LOCATION NAME|WMO CODE
Adelaide|ADL <br>
Albany|ALH <br>
Albury|ABX <br> 

###output of the application is model data written at this location:
>src/main/resources/output/weather-data.csv

####Sample output
######Location|Position|Local Time|Conditions|Temperature|Pressure|Humidity
DRW|-12.414, 130.881, 27.25|2016-08-30T21:22:22+09:30|Rainy|+46.00|996|84 <br>
ASP|-23.795, 133.888, 548.0|2016-08-30T21:22:22+09:30|Sunny|+1.00|1016|14 <br>
BNE|-27.4, 153.1333, 1.02|2016-08-30T21:52:22+10:00|Sunny|+26.00|997|52 <br>

###All the testcases can be run as follows
>sbt test

###Main application can also run as follows, if we were run the application with other inputs
>sbt "run path_to_input_file path_output_file"

