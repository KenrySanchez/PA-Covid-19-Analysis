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
import org.apache.spark.SparkConf

object App {

  /**
   * PRIVATE ATTRIBUTES
   */

  var spark:SparkSession = SparkSession.builder().appName("Twitter_Spark_App").getOrCreate()
  var sparkContext:SparkContext = new SparkContext
  val delimiter = "##"

  /**
   * MAIN
   */

  def main(args: Array[String]) {

    if (sparkContext.isLocal) {

      val config = new SparkConf

      //TODO: Entender estas llaves
      config.set("spark.broadcast.compress", "false")
      config.set("spark.shuffle.compress", "false")
      config.set("spark.shuffle.spill.compress", "false")

      spark = SparkSession.builder().config(config)
        .master("local[2]").appName("Twitter_Spark_App").getOrCreate()
        
      sparkContext = spark.sparkContext
    }

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
