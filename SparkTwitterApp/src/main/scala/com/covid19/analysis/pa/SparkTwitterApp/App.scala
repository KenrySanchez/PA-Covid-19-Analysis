package com.covid19.analysis.pa.SparkTwitterApp

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import java.io.StringWriter
import au.com.bytecode.opencsv.CSVWriter

import collection.JavaConverters._

import spray.json._
import DefaultJsonProtocol._

/**
 * @author KASV
 */

object TwitterModelProtocol extends DefaultJsonProtocol {
  implicit val colorFormat = jsonFormat4(TwitterModel)
}

import TwitterModelProtocol._

object App {

  /**
   * PRIVATE ATTRIBUTES
   */

  //TODO: Try to optimize it later to work in docker
  val FILE_PATH = "/Users/kenrysanchez/DEV/PA-Covid-19-Analysis/resources/tweets/demo/demo_tweet.json"
  val spark = SparkSession.builder().master("local").appName("Twitter_Spark_App").getOrCreate()
  val sparkContext = spark.sparkContext
  val delimiter = "##"

  /**
   * MAIN
   */

  def main(args: Array[String]) {

    val jsonFile = sparkContext.textFile(FILE_PATH).reduce(_ + _)
    val delimitedFile = jsonFile.replaceAll(" ", "").replaceAll("\\}\\{", "\\}\\##\\{")
    
    val rdd = sparkContext.parallelize(List(delimitedFile))

    val filesRdd = rdd.flatMap(f => f.toString().split(delimiter))

    val stringWritter = new StringWriter()
    var csvFile = new CSVWriter(stringWritter)
    
    val twitterRdd = filesRdd.map(json => {

      try {

        //TODO: Revisar esto ya que esta dando error al momento de serializar el objeto
        val jsonParser = json.parseJson
        Some(jsonParser.convertTo[TwitterModel])

      } catch {
        case t: Throwable => None
      }

    })

    twitterRdd.collect().foreach((i) => {

      i match {
        case Some(value) => println(value)
        case None => println("Error kenry")
      }
    })

    twitterRdd.map(model => {

      model match {
        case Some(value) => List(value.id, value.name, value.entryDate, value.tweet).toArray
      }
    }).mapPartitions(list => {

      csvFile.writeAll(list.toList.asJava)
      Iterator(stringWritter.toString)

    }).saveAsTextFile("~/Downloads")

  }

}
