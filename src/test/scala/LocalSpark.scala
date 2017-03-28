package com.mprescha.json2Table

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.scalatest.{Suite, BeforeAndAfterAll}

trait LocalSpark extends BeforeAndAfterAll { this: Suite =>

  implicit val spark = SparkSession.builder().master("local[4]").appName("unitTest").getOrCreate()

  override protected def afterAll(): Unit = spark.stop()

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
}