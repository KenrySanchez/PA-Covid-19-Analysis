

package com.covid19.analysis.pa.SparkTwitterApp

import spray.json.DefaultJsonProtocol

/**
 * @author KASV
 */

case class RetweetModel(created_at: Option[String], text: Option[String], in_reply_to_user_id_str: Option[String],
    )