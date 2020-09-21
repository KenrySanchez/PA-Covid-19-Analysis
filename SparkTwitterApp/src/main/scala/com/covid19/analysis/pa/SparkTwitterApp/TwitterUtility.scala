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
   * Function to create a new list with values from the twitterModel.
   *
   * @param model twitter model to serve as a source from the new list.
   *
   * @return List of String based on Twitter attributes.
   */
  def buildTwitterWrapperList(model: TwitterModel): Array[String] = {
    List(model.id_str, model.text, model.created_at, model.source).toArray
  }

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

  /**
   * Function to create CSV file from iterator.
   * 
   * @param list Iterator of strings.
   * 
   * @return CSV file written by an iterator.
   */
  def listToCSVFile(list: Iterator[Array[String]]) = {

    val stringWritter = new StringWriter();
    val csvFile = new CSVWriter(stringWritter)

    csvFile.writeAll(list.toList.asJava)
    Iterator(stringWritter.toString)
  }
}