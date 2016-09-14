package com.jpreddy.tests.featuresgen.weather.datagen

import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import com.jpreddy.tests.featuresgen.weather.utils.TransformationUtil._
import com.jpreddy.tests.featuresgen.weather.CommonDefs._
import scala.util.{Try,Success,Failure}
import com.jpreddy.tests.featuresgen.weather.datamodel._
import com.jpreddy.tests.featuresgen.weather.datamodel.WeatherElementModel._
import com.jpreddy.tests.featuresgen.weather.datagen._
import com.jpreddy.tests.featuresgen.weather.utils.TransformationUtil._
/**
 * This class is responsible for generating atmosphere data such as TEMPERATURE, PRESSURE, HUMIDITY 
 * 
 */
object AtmosphereDataGen {
  
  def generateAtmosphereModel( WeatherStations_GeographyModel: List[(WeatherStation, GeographyModel)])={
    
    WeatherStations_GeographyModel.map(x => (x._1, x._2 ,generateAtmophereData(x._2)  ))
  }
  
  
  def  generateAtmophereData(geoModel : GeographyModel) :AtmosphereModel =
  {
    val temperature = generateTemperature(geoModel)
    val pressure = generatePressure(geoModel)
    val humidity = generateHumidity(geoModel)
    val weatherCondition =generateWeatherCondition(temperature, pressure, humidity)
    AtmosphereModel(temperature,pressure,humidity, weatherCondition)
  }
  /**
   * Global maximum and minimum values are noted down
   * and the value in the min-max range is generated randomly.
   * Timezone is taken into consideration not to skew the data much from reality
   */
  def generateTemperature(geoModel : GeographyModel) :String=
  {
    //generate random number between max and min  temperatures noted on earth
    val minval = Temperature.getTemperature(geoModel.timeZone).min
    val maxval = Temperature.getTemperature(geoModel.timeZone).max
    val range = maxval - minval
    val tempI= scala.util.Random.nextInt(range.toInt)
    val tempF= scala.util.Random.nextFloat
    val tempIF =tempI + tempF
    
    val tempNew = tempIF - (- minval )
    roundAt1(tempNew.toFloat).toString
  }
  
  /**
   * Global maximum and minimum values are noted down
   * and the value in the min-max range is generated randomly.
   * Timezone is taken into consideration not to skew the data much from reality
   */
  def generatePressure(geoModel : GeographyModel) :String=
  {
    //generate random number between max and min  temperatures noted on earth
    val minval = Pressure.getPressure(geoModel.timeZone).min
    val maxval = Pressure.getPressure(geoModel.timeZone).max
    val range = maxval - minval
    val valI  = scala.util.Random.nextInt(range.toInt)
    val valF  = scala.util.Random.nextFloat
    val valIF = valI + valF
    val valNew =  minval +valIF 
    roundAt1(valNew.toFloat).toString
  }
  
  /**
   * Global maximum and minimum values are noted down
   * and the value in the min-max range is generated randomly.
   * Timezone is taken into consideration not to skew the data much from reality
   */
  
  def generateHumidity(geoModel : GeographyModel) :String=
  {
    //generate random number between max and min  temperatures noted on earth
    val minval = Humidity.getHumidity(geoModel.timeZone).min
    val maxval = Humidity.getHumidity(geoModel.timeZone).max
    val range = maxval - minval
    val valI= scala.util.Random.nextInt(range.toInt)
    val valNew = valI - (- minval )
    
    valNew.round.toString
  }
  
 /**
  *  WeatherCondition is calculated from atmosphere elements (Temperature Pressure, Humidity)
  *  Humidty  >= 55 && < 100 is assumed to cause 'Rain'
  *  And with such condition if temperature is in freezing level, it assumed to 'Snow'
  *  Otherwise 'Sunny'
  */
  def generateWeatherCondition(temperature:String, pressure:String, humidity:String):String =
  {
    logger.debug(s"temperature=$temperature, pressure=$pressure, humidity=$humidity")
    val weatherCondition =(temperature.toFloat,pressure.toFloat,humidity.toInt) match {
      case (t,p,h)  if h <=100 && h >=55 && t > 0 =>  "Rain"
      case (t,p,h)  if h <=100 && h >=55 && t <= 0 =>  "Snow"
      case _  => "Sunny"
    }
    
    weatherCondition
    
  }
  
  
  
}