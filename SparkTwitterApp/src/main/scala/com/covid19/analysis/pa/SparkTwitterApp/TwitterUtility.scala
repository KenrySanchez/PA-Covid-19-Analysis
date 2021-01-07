package com.covid19.analysis.pa.SparkTwitterApp

import spray.json._
import DefaultJsonProtocol._

import collection.JavaConverters._

import org.apache.spark.rdd.RDD

/**
 * @author KASV
 */


import TwitterModelProtocol._
import com.univocity.parsers.csv.CsvWriter
import java.text.ParseException
import org.apache.spark.SparkConf
import java.io.StringWriter

import au.com.bytecode.opencsv.CSVWriter
import scala.collection.Iterator

object TwitterUtility {

  /**
   * Function to create the model wrapper from json object.
   *
   * @param json Json object as string to parse.
   *
   * @return Twitter Wrapper Model.
   *
   * @throws None option
   */
  def buildTwitterModelFromJson(json: String) = {

    try {

      val jsonParser = json.parseJson
      Some(jsonParser.convertTo[TwitterModel])

    } catch {
      case t: Throwable => None
    }

  }
}