package com.covid19.analysis.pa.SparkTwitterApp

import spray.json.DefaultJsonProtocol

/**
 * @author KASV
 */

case class TwitterModel(source: String, id_str: String, created_at: String, text: String)