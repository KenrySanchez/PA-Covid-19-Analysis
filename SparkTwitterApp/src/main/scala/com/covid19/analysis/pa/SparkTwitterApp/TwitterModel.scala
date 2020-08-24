package com.covid19.analysis.pa.SparkTwitterApp

import spray.json.DefaultJsonProtocol

case class TwitterModel(id: String, name: String, entryDate: String, tweet: String)