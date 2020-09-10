package com.covid19.analysis.pa.SparkTwitterApp

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import java.io.StringWriter
import au.com.bytecode.opencsv.CSVWriter

import com.covid19.analysis.pa.SparkTwitterApp.TwitterUtility.buildTwitterWrapperList;

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
import com.univocity.parsers.csv.CsvWriter
import java.text.ParseException
import org.scalatools.testing.Logger

object App {

  /**
   * PRIVATE ATTRIBUTES
   */
  
  val spark = SparkSession.builder().master("local").appName("Twitter_Spark_App").getOrCreate()
  val sparkContext = spark.sparkContext
  val delimiter = "##"

  /**
   * MAIN
   */

  def main(args: Array[String]) {
        
    val jsonFile = sparkContext.textFile(args(0)).reduce(_ + _)
    val delimitedFile = jsonFile.replaceAll(" ", "").replaceAll("\\}\\{", "\\}\\##\\{")
    
    val rdd = sparkContext.parallelize(List(delimitedFile))

    val filesRdd = rdd.flatMap(f => f.toString().split(delimiter))
    
    val twitterRdd = filesRdd.map(json => {

      try {

        val jsonParser = json.parseJson
        Some(jsonParser.convertTo[TwitterModel])

      } catch {
        case t: Throwable => None
      }

    })

    twitterRdd.map(model => {
      
      model match {
        case Some(value) => buildTwitterWrapperList(value)
        case None => throw new Exception("Parse Json was not allowed")
      }
    }).mapPartitions(list => {
      
      val stringWritter = new StringWriter();
      val csvFile = new CSVWriter(stringWritter)

      csvFile.writeAll(list.toList.asJava)
      Iterator(stringWritter.toString)

    }).saveAsTextFile(args(1))

  }

}
