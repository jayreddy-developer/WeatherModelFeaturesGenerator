package com.jpreddy.tests.featuresgen.weather.datagen

import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import com.jpreddy.tests.featuresgen.weather.datavalidation.DataValidationUtil;
import com.jpreddy.tests.featuresgen.weather.utils.JSONParser;
import com.jpreddy.tests.featuresgen.weather.utils.TransformationUtil._
import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import scala.util.{Try,Success,Failure}
import com.jpreddy.tests.featuresgen.weather.datasource.GeographyDataSource
import com.jpreddy.tests.featuresgen.weather.datamodel._
import com.jpreddy.tests.featuresgen.weather.utils._
import com.jpreddy.tests.featuresgen.weather.utils.TransformationUtil._

/**
 * This class is responsible for generating geography data such as LATITUDE, LONGITUDE, ELEVATION 
 * data is not generated but the actual data is fetched from remote location.
 * As this remote file contains whole lot of other data, only selective data is extracted.
 */

object GeographyDataGen {
  
  /**
   * GeographyModel  object is created for the  WeatherStation  by parsing the data from a remote file
   * 
   */
  def fetchGeographyData(weatherStations: List[WeatherStation]): List[(WeatherStation, GeographyModel)] =
  {
    GeographyDataSource.fetchMLIDMappingFile
    //4. Parse MLID file and filter the COORDINATES needed for INPUT WMO stations
   val iataCodeHeader="iata_xref"
   val latitudeHeader="lat_prp"
   val longitudeHeader="lon_prp"
   val timeZone ="tz"
   val iata_data = Try(scala.io.Source.fromFile(mlidLocalLocation, "ISO-8859-1").getLines().dropWhile(!_.startsWith("country3")).toArray) match {
       case Success(array) => array
       case Failure(f) => { logger.error(s"Error in processing IATA MLID mapping file $mlidLocalLocation : $f") ; Array() }
   }
    
   val header = (iata_data.take(1).map{x => x.split(",")}.map{x => (x.indexOf(iataCodeHeader), x.indexOf(latitudeHeader),x.indexOf(longitudeHeader), x.indexOf(timeZone))})
   val IATA =header(0)._1
   val LATITUDE =header(0)._2
   val LONGITUDE =header(0)._3
   val TZ = header(0)._4
   val iataSet = weatherStations.map(x => (x.iata)).toSet
   val iata_geography_mapping_collected =iata_data.drop(1).map(x => x.split(",")).filter(x => iataSet.contains(x(IATA)))
   .map(x =>  (x(IATA),GeographyModel(x(LATITUDE).toFloat, x(LONGITUDE).toFloat,0f, x(TZ))) )
    
   val iata_geography_mapping = DataValidationUtil.removeDuplicates(iata_geography_mapping_collected).toList.sortBy( x => x._1)
  
   //Pair  WeatherStationsModel with GeographyModel
   val weatherStations_mapping = weatherStations.sortBy( x => x.iata)
    
    val result =  weatherStations_mapping.zip(iata_geography_mapping.map(x => x._2))
    //Fetch Eelevation info based on the latitude and longitude and update the GeographyModel
    val geographyModel = result.map(x => (x._1, x._2,fetchElevation(x._2.latitude.toString, x._2.longitude.toString)))
    .map(x => ( x._1,GeographyModel(roundAt2(x._2.latitude), roundAt2(x._2.longitude), x._3.toFloat.round, x._2.timeZone )))
    
    geographyModel
   }
  
  /**
   * Elevation data for GEO coordinates is fetched separately from another source
   */
   def fetchElevation(lat:String, lon: String) ={
     
   //FETCH ELEVATION 
      val elevationResponse= GeographyDataSource.fetchElevation(elevtionAPI,lat,lon)
      val elevationData = JSONParser.parseElevationAPIResponse(elevationResponse)
      
      val elevation = elevationData match {
        case Success(elevationInfo) => elevationInfo
        case Failure(f) => {  logger.error(s"Failure in parsing elevation response for $lat ,$lon = $f  ; API response=$elevationResponse") ; ""  }
      }
      
      elevation
   }
   
}