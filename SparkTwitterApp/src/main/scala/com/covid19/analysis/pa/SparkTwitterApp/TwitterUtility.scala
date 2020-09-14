package com.covid19.analysis.pa.SparkTwitterApp

/**
 * @author KASV
 */

object TwitterUtility {
  
  def buildTwitterWrapperList(value: TwitterModel): Array[String] = {
    List(value.id_str, value.text, value.created_at, value.source).toArray
  }
}