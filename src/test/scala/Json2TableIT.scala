package com.mprescha.json2Table

import org.scalatest.{MustMatchers, FlatSpec}

/**
  * Created by mprescha on 28.03.17.
  */
class Json2TableIT extends FlatSpec with LocalSpark with MustMatchers{

  behavior of "Json2TableIT"

  it should "transformBusinessToTables without loss of rows" in {
    val businessInputFile = getClass.getResource("/business.json").getFile
    val outputPath = getClass.getResource("/").getFile + "output/businessAsTable"

    Json2Table.transformBusinessToTables(businessInputFile,outputPath)
    spark.read.parquet(outputPath).count() must be (200)
  }

}
