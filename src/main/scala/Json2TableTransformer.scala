package com.mprescha.json2Table

import org.apache.spark.sql.{Dataset, SparkSession}


/**
  * Created by mprescha on 28.03.17.
  */
class Json2TableTransformer {

  def businessAsTable(businessRaw:Dataset[Business])(implicit spark:SparkSession) : Dataset[BusinessTable] = {
    import spark.implicits._
    val ds: Dataset[BusinessTable] = businessRaw.map (b => BusinessTable(
      business_id = b.business_id,
      name= b.name,
      neighborhood= b.neighborhood,
      address = b.address,
      city=  b.city,
      state=  b.state,
      postal_code=  b.postal_code,
      latitude= b.latitude,
      longitude=  b.longitude,
      stars=  b.stars,
      review_count=  b.review_count,
      is_open=  b.is_open,
      attributes= ArrayConverter.arrayAsString(b.attributes),
      categories=  ArrayConverter.arrayAsString(b.categories) ,
      hours=  ArrayConverter.arrayAsString(b.hours),
      `type`=  b.`type`
    ))
    return ds
  }

  def businessAttributes(businessRaw:Dataset[Business])(implicit spark:SparkSession) : Dataset[BusinessAttributes] = {
    import spark.implicits._
    val ds  = businessRaw
      .select( $"business_id".as[String], $"attributes".as[Array[String]])
      .withColumn("attribute", org.apache.spark.sql.functions.explode(businessRaw.col("attributes"))).drop("attributes").as[BusinessAttributes]
    return ds
  }

  def businessCategories(businessRaw:Dataset[Business])(implicit spark:SparkSession) : Dataset[BusinessCategories] = {
    import spark.implicits._
    val ds  = businessRaw
      .select( $"business_id".as[String], $"categories".as[Array[String]])
      .withColumn("categorie", org.apache.spark.sql.functions.explode(businessRaw.col("categories"))).drop("categories").as[BusinessCategories]
    return ds
  }

  def businessHours(businessRaw:Dataset[Business])(implicit spark:SparkSession) : Dataset[BusinessHours] = {
    import spark.implicits._
    val ds  = businessRaw
      .select( $"business_id".as[String], $"hours".as[Array[String]])
      .withColumn("hour", org.apache.spark.sql.functions.explode(businessRaw.col("hours"))).drop("hours").as[BusinessHours]
    return ds
  }

  /*
    CHECKIN
   */
  def checkinAsTable(checkin:Dataset[Checkin])(implicit spark:SparkSession) : Dataset[CheckinTable] = {
    import spark.implicits._
    val ds = checkin.map (x => CheckinTable(
      business_id = x.business_id,
      time = ArrayConverter.arrayAsString(x.time),
      `type`=  x.`type`
    ))
    return ds
  }

  def checkinTimes(checkin:Dataset[Checkin])(implicit spark:SparkSession) : Dataset[CheckinTimes] = {
    import spark.implicits._
    val ds  = checkin
      .select( $"business_id".as[String], $"time".as[Array[String]])
//      .withColumnRenamed("time","times")
      .withColumn("time", org.apache.spark.sql.functions.explode(checkin.col("time")))/*.drop("times")*/.as[CheckinTimes]
    return ds
  }

  /*
    USER
   */

  def userFriends(user:Dataset[User])(implicit spark:SparkSession) : Dataset[UserFriends] = {
    import spark.implicits._
    val ds = user
      .select($"user_id".as[String], $"friends".as[Array[String]])
      .withColumn("friend", org.apache.spark.sql.functions.explode(user.col("friends"))).drop("friends").as[UserFriends]
    return ds
  }
  def userElite(user:Dataset[User])(implicit spark:SparkSession) : Dataset[UserElite] = {
    import spark.implicits._
    val ds = user
      .select($"user_id".as[String], $"elite".as[Array[String]])
      .withColumn("elite", org.apache.spark.sql.functions.explode(user.col("elite"))).as[UserElite]
    return ds
  }
}
