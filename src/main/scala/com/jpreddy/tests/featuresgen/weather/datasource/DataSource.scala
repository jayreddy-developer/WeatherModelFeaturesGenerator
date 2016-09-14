package com.jpreddy.tests.featuresgen.weather.datasource

import com.jpreddy.tests.featuresgen.weather.CommonDefs._

import java.net.URL
import java.io.File

import sys.process._

class DataSource {
  
   def copyRemoteFile(remoteLocation:String, localFile:String)= {
    logger.debug(s"copyRemoteFile=> remoteLocation=$remoteLocation ; localFile=$localFile")
  new URL(remoteLocation) #> new File(localFile) !!
  }
}