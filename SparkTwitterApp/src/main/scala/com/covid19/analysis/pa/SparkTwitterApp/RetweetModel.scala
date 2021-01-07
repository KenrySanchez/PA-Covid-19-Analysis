

package com.covid19.analysis.pa.SparkTwitterApp

import spray.json.DefaultJsonProtocol

/**
 * @author KASV
 */

//case class RetweetModel(in_reply_to_user_id_str: String,
//    in_reply_to_status_id_str: String, Text: String, created_at: String)

case class RetweetModel(created_at: Option[String], text: Option[String], in_reply_to_user_id_str: Option[String],
    )