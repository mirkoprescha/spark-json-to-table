package com.mprescha.json2Table
import sys.process._
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by mprescha on 28.03.17.
  */
object Json2Table {
  val inputFiles = Map(
    "business" -> "yelp_academic_dataset_business.json",
    "checkin" -> "yelp_academic_dataset_checkin.json",
    "review" -> "yelp_academic_dataset_review.json",
    "tip" -> "yelp_academic_dataset_tip.json",
    "user" -> "yelp_academic_dataset_user.json"
  )

  def main(args: Array[String]) {
    if (args.length != 1) {
      println ("Please provide file name including path to dataset challenge file.")
    }
    val fullPathToTar = args(0)

    println (s"reading tar from $fullPathToTar")
    unzipTarFile(fullPathToTar)

    val spark = SparkSession.builder().master("local[4]").appName(s"json2table").getOrCreate()
//    val spark = SparkSession.builder().appName(s"json2table").getOrCreate()  //if provided


    transformBusinessToTables(inputFiles.get("business").get,"./output")(spark)
    transformCheckinToTables(inputFiles.get("checkin").get,"./output")(spark)
    transformUserintoTables(inputFiles.get("user").get,"./output")(spark)

    transformJson2Parquet(inputFiles.get("tip").get,"./output/tip")(spark)
    transformJson2Parquet(inputFiles.get("review").get,"./output/review")(spark)
    spark.stop()

  }


  def unzipTarFile (inputFilename: String) = {
    println(s"unzip tar file $inputFilename")
    val result: Int = "ls -al" !
//    println (result.toString)
    val result2: Int = s"tar xopf $inputFilename" !
    // println (s"result $result2")


  }


  /*
    reads json from directory and writes to outputPath as parquet without any changes
   */
  def transformJson2Parquet(inputFilename: String, outputPath: String)(implicit spark:SparkSession) = {
    val df = spark.read.json(inputFilename)
    df.write.mode(SaveMode.Overwrite).parquet(outputPath)
    println (s"Written output to ${outputPath}")
  }

  /*
  reads json file containing business entities and writes all child entities to given outputPath-prefix
  */
  def transformBusinessToTables (inputFilename: String, outputPath: String)(implicit spark:SparkSession) = {
    import spark.implicits._
    val df = spark.read.json(inputFilename)
    println (s"Reading ${df.count()} rows from source file $inputFilename")

    val ds = df.as[Business]
    println ("schema of business validated")

    val result = new Json2TableTransformer().businessAsTable(ds)
    println (s"Converted ${result.count()} rows into table") //TODO: remove count in production

    val outputPathBusinessTable=outputPath + "/businessAsTable"
    result.toDF().write.mode(SaveMode.Overwrite).parquet(outputPathBusinessTable)
    println (s"Written output to $outputPathBusinessTable")

    new Json2TableTransformer().businessAttributes(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/businessAttributes")
    println (s"Written output to ${outputPath + "/businessAttributes"}")
    new Json2TableTransformer().businessCategories(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/businessCategories")
    println (s"Written output to ${outputPath + "/businessCategories"}")
    new Json2TableTransformer().businessHours(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/businessHours")
    println (s"Written output to ${outputPath + "/businessHours"}")

  }


  def transformCheckinToTables (inputFilename: String, outputPath: String)(implicit spark:SparkSession) = {
    import spark.implicits._
    val df = spark.read.json(inputFilename)
    println (s"Reading ${df.count()} rows from source file $inputFilename")

    val ds = df.as[Checkin]
    println ("schema of Checkin validated")

    val result = new Json2TableTransformer().checkinAsTable(ds)
    println (s"Converted ${result.count()} rows into table") //TODO: remove count in production

    val outputPathCheckinTable=outputPath + "/checkinAsTable"
    result.toDF().write.mode(SaveMode.Overwrite).parquet(outputPathCheckinTable)
    println (s"Written output to $outputPathCheckinTable")

    new Json2TableTransformer().checkinTimes(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/checkinTimes")
    println (s"Written output to ${outputPath + "/checkinTimes"}")

  }

  def transformUserintoTables (inputFilename: String, outputPath: String)(implicit spark:SparkSession) = {
    import spark.implicits._
    val df = spark.read.json(inputFilename)
    println (s"Reading ${df.count()} rows from source file $inputFilename")

    val ds = df.as[User]
    println ("schema of Checkin validated")

    ds.write.mode(SaveMode.Overwrite).parquet(outputPath + "/userAsTable")
    println (s"Written output to ${outputPath + "/userAsTable"}")

    new Json2TableTransformer().userElite(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/userElite")
    println (s"Written output to ${outputPath + "/userElite"}")

    new Json2TableTransformer().userFriends(ds).write.mode(SaveMode.Overwrite).parquet(outputPath + "/userFriends")
    println (s"Written output to ${outputPath + "/userFriends"}")
  }
}
