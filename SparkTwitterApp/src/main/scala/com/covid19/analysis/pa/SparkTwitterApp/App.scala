package com.covid19.analysis.pa.SparkTwitterApp

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import com.covid19.analysis.pa.SparkTwitterApp.TwitterUtility._;

import collection.JavaConverters._

import spray.json._
import DefaultJsonProtocol._

/**
 * @author KASV
 */

object TwitterModelProtocol extends DefaultJsonProtocol {

  implicit val retweetFormat = lazyFormat(jsonFormat(
    RetweetModel,
    "created_at", "text", "in_reply_to_user_id_str"))

  implicit val twitterFormat: JsonFormat[TwitterModel] = lazyFormat(jsonFormat(TwitterModel, "source",
    "id_str", "created_at", "text",
    "reply_count", "favorite_count", "quote_count", "retweeted_status"))
}

import TwitterModelProtocol._
import java.text.ParseException
import org.apache.spark.SparkConf
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SQLContext

object App {

  /**
   * PRIVATE ATTRIBUTES
   */

  var spark: SparkSession = SparkSession.builder().master("local[2]").appName("Twitter_Spark_App").getOrCreate()
  var sparkContext: SparkContext = spark.sparkContext
  val sqlContext = new SQLContext(sparkContext)
  val delimiter = "##"

  /**
   * MAIN
   */

  def main(args: Array[String]) {
        
    if (sparkContext.isLocal) {

      spark.conf.set("spark.broadcast.compress", "false")
      spark.conf.set("spark.shuffle.compress", "false")
      spark.conf.set("spark.shuffle.spill.compress", "false")

    }

    val jsonFile = sparkContext.textFile(args(0)).reduce(_ + _)
    val delimitedFile = jsonFile.replaceAll(" ", "").replaceAll("\\}\\{", "\\}\\##\\{")

    val rdd = sparkContext.parallelize(List(delimitedFile))

    val filesRdd = rdd.flatMap(f => f.toString().split(delimiter))

    //TODO: Fixing to use spark dataframes
    val twitterRdd = filesRdd.map(json => buildTwitterModelFromJson(json))

    val twitterRdd2 = twitterRdd.map(model => {

      model match {
        case Some(value) => (value)
        case None => throw new Exception("Parse Json was not allowed")
      }
    })

    val twitterDataframe = sqlContext.createDataFrame(twitterRdd2)

    twitterDataframe.createOrReplaceTempView("twitter_table")

    sqlContext.sql("select * from twitter_table").show()

  }

}
