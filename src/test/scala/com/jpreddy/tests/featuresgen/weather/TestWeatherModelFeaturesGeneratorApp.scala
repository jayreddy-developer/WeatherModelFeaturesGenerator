package com.jpreddy.tests.featuresgen.weather

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import  com.jpreddy.tests.featuresgen.weather.datamodel.WeatherDataModel._
import com.jpreddy.tests.featuresgen.weather.datamodel.WeatherStation
import  com.jpreddy.tests.featuresgen.weather.CommonDefs._

class TestWeatherModelFeaturesGeneratorApp extends FlatSpec {
  
 
  "WeatherModelFeaturesGeneratorApp" should "generate weather data reading weatherstations from a file and write the generated model data to a file" in {
    
      
    val args = Array(iataFile, weatherModelDataFile)
    WeatherModelFeaturesGeneratorApp.main(args)
    
  }

  
  
}