package com.covid19.analysis.pa.test

import org.apache.spark.SharedSparkContext

import com.covid19.analysis.pa.SparkTwitterApp.TwitterUtility._;

import org.scalatest.FunSpec
import org.junit.Test
import org.specs2.specification.BeforeSpec
import org.junit.Before
import scala.io.Source
import com.covid19.analysis.pa.SparkTwitterApp.TwitterModel
import org.apache.spark.sql.test.SharedSQLContext
import org.apache.spark.SparkFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.BeforeAndAfterAll
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/**
 * @author KASV
 */

@Test
class AppTesting extends FunSpec with BeforeAndAfterEach with BeforeAndAfterAll {

  @transient var sc: SparkContext = null
  
  override def beforeAll(): Unit = {

    val envMap = Map[String,String](("Xmx", "512m"))

    val sparkConfig = new SparkConf()
    sparkConfig.set("spark.broadcast.compress", "false")
    sparkConfig.set("spark.shuffle.compress", "false")
    sparkConfig.set("spark.shuffle.spill.compress", "false")
    sc = new SparkContext("local[2]", "unit test", sparkConfig)
  }

  override def afterAll(): Unit = {
    sc.stop()
  }
  
  it("Testing twitter parse/transformation function") {
    val input = this.getClass.getResourceAsStream("/demo_tweet.json")
    val text = Source.fromInputStream(input).getLines().reduce(_ + _)
    
    val rdd = sc.parallelize(List(text))
    val twitterRdd = rdd.map(json => buildTwitterModelFromJson(json))
        
    assert(twitterRdd.collect().length > 0, true)
  }

}