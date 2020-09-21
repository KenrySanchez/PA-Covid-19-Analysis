package com.covid19.analysis.pa.test

import org.apache.spark.SharedSparkContext

import com.covid19.analysis.pa.SparkTwitterApp.TwitterUtility._;

import org.scalatest.FunSpec
import org.junit.Test
import org.specs2.specification.BeforeSpec
import org.junit.Before
import scala.io.Source

/**
 * @author KASV
 */

@Test
class AppTesting extends FunSpec with SharedSparkContext {

  it("testing twitter parse/transformation function") {
    
    val input = this.getClass.getResourceAsStream("/demo_tweet.json")
    
    val text = Source.fromInputStream(input).getLines().reduce(_ + _)

//    val jsonFile = sc.textFile(this.getClass.getResourceAsStream(name)("/demo_tweet.json").getPath).reduce(_ + _)

    val rdd = sc.parallelize(List(text))
    
    val twitterRdd = rdd.map(json => buildTwitterModelFromJson(json)).map(model => {

      model match {
        case Some(value) => buildTwitterWrapperList(value)
        case None => throw new Exception("Parse Json was not allowed")
      }
    })
    
    assert(twitterRdd.collect().length > 0, true)

  }

  it("testing csvFile with data") {

    val arrayList = Array("source", "id", "create", "text")

    val iterable = Iterator(arrayList)

    val csv = listToCSVFile(iterable)

    assert(csv.length > 0, true)

  }

}