package com.jpreddy.tests.featuresgen.weather

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import  com.jpreddy.tests.featuresgen.weather.datamodel.WeatherDataModel._
import com.jpreddy.tests.featuresgen.weather.datamodel.WeatherStation


class TestWeatherModelFeaturesGenerator extends FlatSpec {
  
 
  "WeatherModelFeaturesGenerator" should "generate weather data for the input weather station " in {
    
      val weatherStation = WeatherStation("Alice Springs","ASP")
      val weatherStations =  List[WeatherStation](weatherStation)
  
      val result =WeatherModelFeaturesGenerator.generateWeatherData(weatherStations)
//      println(result)
      assert( result.head.startsWith("ASP|-23.79,133.89,548|") )
      
    
  }

}