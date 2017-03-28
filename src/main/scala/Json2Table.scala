package com.mprescha.json2Table

import org.apache.spark.sql.{SaveMode, SparkSession}

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

  def transformBusinessToTables (inputFilename: String, outputPath: String)(implicit spark:SparkSession) = {
    import spark.implicits._
    val df = spark.read.json(inputFilename)
    println (s"Reading ${df.count()} rows from source file $inputFilename")

    val ds = df.as[Business]
    println ("schema of business validated")

    val result = new Json2TableTransformer().businessAsTable(ds)
    println (s"Converted ${result.count()} rows into table")

    result.toDF().write.mode(SaveMode.Overwrite).json(outputPath + "/businessAsTable")

  }
}
