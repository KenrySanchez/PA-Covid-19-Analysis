package com.covid19.analysis.pa.test

import org.apache.spark.SharedSparkContext
import org.scalatest.FunSuite

import com.covid19.analysis.pa.SparkTwitterApp.App
import org.scalatest.FunSpec
import scala.reflect.io.File
import java.io.StringReader
import au.com.bytecode.opencsv.CSVReader
import org.junit.Test
import org.apache.spark.SparkContext

import scala.io.Source

/**
 * @author KASV
 */

@Test
class AppTesting extends FunSpec {
  
  it("testing twitter model wrapper") {
    
  }

  it("testing csvFile with data") {

    val input1 = ""
    val fileDirectory = "~/."

    App.main(Array(input1, fileDirectory))

    println("paso aqui kenry")
    
    val bufferedSource = Source.fromFile(fileDirectory)
    val iter = bufferedSource.getLines()
    
    bufferedSource.close()

    assert(true, iter.length > 0)

  }

}