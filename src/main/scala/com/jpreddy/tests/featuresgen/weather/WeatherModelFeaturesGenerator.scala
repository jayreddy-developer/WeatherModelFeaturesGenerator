package com.jpreddy.tests.featuresgen.weather

import com.jpreddy.tests.featuresgen.weather.datagen.AtmosphereDataGen;
import com.jpreddy.tests.featuresgen.weather.datagen.GeographyDataGen;
import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import scala.util.{Try,Success,Failure}
import com.jpreddy.tests.featuresgen.weather.datamodel._
import com.jpreddy.tests.featuresgen.weather.datagen._
import com.jpreddy.tests.featuresgen.weather.utils.FileIOUtil._

/**
 * This class encapsulates  whole of the application functionality.
 * 1.Loads the input stations file
 * 2.Generates weather data for them
 * 3.Writes the weather data to output file
 */
object WeatherModelFeaturesGenerator {
  
  def loadWeatherStations(iataFile: String):List[WeatherStation] =
  {
    
   //1. Input feed containing WeatherStationName with WMO code
   logger.info(s"weather stations input file is read from this location =$iataFile")
   val weatherstations = Try{scala.io.Source.fromFile(iataFile)
     .getLines().filter(!_.startsWith("#")).map(_.split("\\|"))
     .map(x =>WeatherStation(x(0),x(1))).toList}  match {
     case Success(list) => list
     case Failure(f) => { logger.error(s"Error in processing IATA codes file $iataFile : $f") ; List[WeatherStation]()}
   }
     
   logger.info(s"The number of weather stations in the input file = ${weatherstations.length}")
     
   weatherstations
  }
  /**
   * generates the weather data for list of stations
   * step1:  Geography Data( latitude, longitude, elevation, timezone) is extracted from a remoted file/api
   * step2:  Atmosphere Data is generated based on possible range of those values (eg: humidity 0-100 )
   * Step3 : All the data is aggregated and the model data is created
   */
  def generateWeatherData(weatherStations :List[WeatherStation]):  List[String]=
  {
   logger.debug("WeatherModelFeaturesGenerator.generateWeatherData started")
   

   //Generate Geography Data
   val weatherStaionGeographyModel= GeographyDataGen.fetchGeographyData(weatherStations)

   //Generate Atmosphere Data
   val aggregatedModelList = AtmosphereDataGen.generateAtmosphereModel(weatherStaionGeographyModel)

   val weatherModel = aggregatedModelList.map(aggregatedModel => WeatherDataModel.createWeatherDataModel(aggregatedModel)).map(_.toString())
   
   logger.info(s"weather model is generated for the input stations")
   
   weatherModel
   }
  
  def storeWeatherData(weatherModel :List[String],outputFilePath: String)=
  {
   
   logger.info(s"weather model data File =  $outputFilePath")
   
   //Generated weather model is written to file
   writeToFile(WeatherDataModel.getHeader, weatherModel,weatherModelDataFilePath) match {
        case Success(status)  => if(status) logger.info(s"model data successfully written to file $weatherModelDataFilePath") 
                                 else logger.info(s"Application data has some issues in writing to file") 
        case Failure(f)  =>  logger.error(s"Error in writng model data to file")
      }
  }
  
}