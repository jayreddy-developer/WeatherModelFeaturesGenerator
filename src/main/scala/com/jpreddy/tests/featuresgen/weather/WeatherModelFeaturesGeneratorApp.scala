package com.jpreddy.tests.featuresgen.weather

import com.jpreddy.tests.featuresgen.weather.CommonDefs._

object WeatherModelFeaturesGeneratorApp {
  
  /**
   * This is the main class to invoke the application
   * It take two inputs
   *   paramter1 : InputFilePath ( File containing WeatherStationName and its WMO code )
   *   paramter2 : outFilePath   ( File where the generated model to be written to )
   * 
   */
  def main(args :Array[String])=
  {
    logger.debug("WeatherModelFeaturesGeneratorApp starting")
    
    val Array(inputStationsFile,weatherDataFilePath) = args
    
    val weatherStations = WeatherModelFeaturesGenerator.loadWeatherStations(inputStationsFile)
    val weatherModel = WeatherModelFeaturesGenerator.generateWeatherData(weatherStations)
    
    WeatherModelFeaturesGenerator.storeWeatherData(weatherModel,weatherDataFilePath)
    
    logger.debug("WeatherModelFeaturesGeneratorApp completed")
  }
  
}