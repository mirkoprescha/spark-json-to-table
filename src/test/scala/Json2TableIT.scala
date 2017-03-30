package com.mprescha.json2Table

import org.scalatest.{MustMatchers, FlatSpec}

/**
  * Created by mprescha on 28.03.17.
  */
class Json2TableIT extends FlatSpec with LocalSpark with MustMatchers{

  behavior of "Json2TableIT"

  it should "transformBusinessToTables without loss of rows" in {
    val businessInputFile = getClass.getResource("/business.json").getFile
    val outputPath = getClass.getResource("/").getFile + "output"

    Json2Table.transformBusinessToTables(businessInputFile,outputPath)
    spark.read.parquet(outputPath + "/businessAsTable").count() must be (200)
    spark.read.parquet(outputPath + "/businessAsTable").select("business_id").distinct()count() must be (200)

    println(spark.read.parquet(outputPath + "/businessAttributes").select("business_id").distinct()count()  must be (177))
  }


  it should "transform checkin data to tables without loss of rows" in {
    val inputFile = getClass.getResource("/checkin.json").getFile
    val outputPath = getClass.getResource("/").getFile + "output"

    Json2Table.transformCheckinToTables(inputFile,outputPath)
    spark.read.parquet(outputPath + "/checkinAsTable").count() must be (40)
    spark.read.parquet(outputPath + "/checkinAsTable").select("business_id").distinct()count() must be (40)

    spark.read.parquet(outputPath + "/checkinTimes").select("business_id").distinct()count()  must be (40)
  }

  it should "transform user data to tables without loss of rows" in {
    val inputFile = getClass.getResource("/user.json").getFile
    val outputPath = getClass.getResource("/").getFile + "output"

    Json2Table.transformUserintoTables(inputFile,outputPath)
    spark.read.parquet(outputPath + "/userAsTable").count() must be (5)
    spark.read.parquet(outputPath + "/userAsTable").select("user_id").distinct()count() must be (5)

    spark.read.parquet(outputPath + "/userElite").select("user_id").distinct()count()  must be (5)
    spark.read.parquet(outputPath + "/userFriends").select("user_id").distinct()count()  must be (5)
  }


}
