package com.mprescha.json2Table

import org.scalatest.FlatSpec

/**
  * Created by mprescha on 28.03.17.
  */
class Json2TableIT extends FlatSpec with LocalSpark{

  behavior of "Json2TableIT"

  it should "transformBusinessToTables" in {
    val businessInputFile = "/data/home/mprescha/git/spark-json-to-table/src/test/resources/business.json"
    val outputPath = "/data/home/mprescha/git/spark-json-to-table/target/output"
    Json2Table.transformBusinessToTables(businessInputFile,outputPath)

  }

}
