package com.mprescha.json2Table

import org.apache.spark.sql.SparkSession

/**
  * Created by mprescha on 28.03.17.
  */
object Json2Table {

  def main(args: Array[String]) {
    if (args.length != 1) {
      println ("Please provide file name including path to dataset challenge file.")
    }
    val fullPathToTar = args(0)

    println (s"reading tar from $fullPathToTar")
    val spark = SparkSession.builder().appName(s"json2table").getOrCreate()

    //transformBusinessToTables("xxxx")
    spark.stop()

  }

  def transformBusinessToTables (filename: String)(implicit spark:SparkSession) = {
    val df = spark.read.json(filename)
    println (s"Reading ${df.count()} rows from source file $filename")

    //new Json2TableTransformer
  }
}
