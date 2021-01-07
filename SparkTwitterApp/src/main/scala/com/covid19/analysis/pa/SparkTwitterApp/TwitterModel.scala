package com.covid19.analysis.pa.SparkTwitterApp

import spray.json.DefaultJsonProtocol

/**
 * @author KASV
 */

case class TwitterModel(source: Option[String], id_str: Option[String], created_at: Option[String], text: Option[String],
    reply_count: Option[Int], favorite_count: Option[Int], quote_count: Option[Int], retweeted_status: Option[RetweetModel])