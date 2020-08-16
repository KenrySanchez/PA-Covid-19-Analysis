package com.covid19.analysis.pa.SparkTwitterApp

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.collection.mutable.MutableList

/**
 * @author KASV
 */

object App {

  /**
   * PRIVATE ATTRIBUTES
   */

  //TODO: Try to optimize it later to work in docker
  val FILE_PATH = "/Users/kenrysanchez/DEV/PA-Covid-19-Analysis/resources/tweets/demo/demo_tweet.json"
  val spark = SparkSession.builder().master("local").appName("Twitter_Spark_App").getOrCreate()
  val sparkContext = spark.sparkContext
  val sqlContext = spark.sqlContext
  val delimiter = "} "

  /**
   * MAIN
   */

  def main(args: Array[String]) {

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    val jsonFile = sparkContext.textFile(FILE_PATH)
    val files = jsonFile.flatMap(f => f.toString().split(delimiter))

    files.map(json => {

      try {

        val model = mapper.readValue(json, classOf[TwitterModel])
        
      } catch {
        case t: Throwable => None
      }

    })
    
    println(files.collect())
  }

}
