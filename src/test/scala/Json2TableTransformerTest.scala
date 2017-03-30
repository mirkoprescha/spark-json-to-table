package com.mprescha.json2Table

import org.apache.spark.sql.Dataset
import org.scalatest.{FlatSpec, MustMatchers}

import scala.collection.immutable.List

/**
  * Created by mprescha on 28.03.17.
  */
class Json2TableTransformerTest extends FlatSpec with MustMatchers with LocalSpark{
  import spark.implicits._
  val businesses: Dataset[Business] = spark.createDataset(List(
    Business(
      business_id = "1",
      name= "myName",
      neighborhood= "myNeighborhood",
      address = "myAddress",
      city=  "myCity",
      state=  Some("myState"),
      postal_code=  "",
      latitude=  55.5,
      longitude=  33.3,
      stars=  2.0,
      review_count=  5,
      is_open=  1,
      attributes= Array("1","2","3","4"),
      categories=  Array("a","d","c","b"),
      hours=  Array("x","y","z"),
      `type`=  "myType"
    )
  ))

  val checkin: Dataset[Checkin] = spark.createDataset(List(
    Checkin(
      business_id = "1",
      time =  Array("Wed-0:1","Tue-21:1","Sat-23:1"),
      `type`=  "myType"
    ),
    Checkin(
      business_id = "2",
      time =  Array("Tue","Sun"),
      `type`=  "checkin"
    )
  ))

  val checkin_empty: Dataset[Checkin] = spark.createDataset(List(
    Checkin(
      business_id = "1",
      time =  Array("Wed-0:1","Tue-21:1","Sat-23:1"),
      `type`=  "myType"
    ),
    Checkin(
      business_id = "2",
      time =  null,
      `type`=  "checkin"
    )
  ))

  val testUser = User(
    user_id=  "1",
    name=  "name",
    review_count=  1,
    yelping_since=  "2017",
    friends=  Array("friend1","friend2"),
    useful=  3,
    funny=  4,
    cool=  1,
    fans=  2.2,
    elite=  Array("elite1"),
    average_stars=  1,
    compliment_hot=  2.2,
    compliment_more=  3,
    compliment_profile=  4,
    compliment_cute=  5,
    compliment_list=  6,
    compliment_note=  7,
    compliment_plain=  8,
    compliment_cool=  9,
    compliment_funny=  10,
    compliment_writer=  11,
    compliment_photos=  12,
    `type`=  "myType"
  )


  "Arrays of Business Class " must   "be converted in comma separated String" in {
    println ("schema of business raw data based on json:")
    businesses.printSchema()
    println("input business raw data:")
    businesses.show(false)

    val underTest: Dataset[BusinessTable] = new Json2TableTransformer().businessAsTable(businesses)

    println (underTest.head().attributes)
    underTest.head().attributes must be ("1,2,3,4")
    underTest.head().categories must be ("a,d,c,b")
    underTest.head().hours must be ("x,y,z")
  }

  "Elements of Business Class " must   "be passed into BusinessTable without changes" in {
    println ("schema of business raw data based on json:")
    businesses.printSchema()
    println("input business raw data:")
    businesses.show(false)

    val underTest: Dataset[BusinessTable] = new Json2TableTransformer().businessAsTable(businesses)

    println("output business table data:")
    underTest.show(false)
    underTest.head().business_id must be ("1")
    underTest.head().name must be ("myName")
    underTest.head().neighborhood must be ("myNeighborhood")
    underTest.head().address must be ("myAddress")
    underTest.head().city must be ("myCity")
    underTest.head().state must be (Some("myState"))
    underTest.head().postal_code must be ("")
    underTest.head().latitude must be (55.5)
    underTest.head().longitude must be (33.3)
    underTest.head().stars must be (2.0)
    underTest.head().review_count must be (5)
    underTest.head().is_open must be (1)
    underTest.head().`type` must be ("myType")
  }

  "Attribute-Array in Business Class " must "be exploded into several rows" in {
    val underTest = new Json2TableTransformer().businessAttributes(businesses).sort($"business_id",$"attribute")
    underTest.show(false)
    underTest.count() must be (4)
    underTest.head(4)(0) must be (BusinessAttributes("1","1"))
    underTest.head(4)(1) must be (BusinessAttributes("1","2"))
    underTest.head(4)(2) must be (BusinessAttributes("1","3"))
    underTest.head(4)(3) must be (BusinessAttributes("1","4"))
  }




  "Categorie-Array in Business Class " must "be exploded into several rows" in {
    val underTest = new Json2TableTransformer().businessCategories(businesses).sort($"business_id",$"categorie")
    underTest.show(false)
    underTest.count() must be (4)
    underTest.head(4)(0) must be (BusinessCategories("1","a"))
    underTest.head(4)(1) must be (BusinessCategories("1","b"))
    underTest.head(4)(2) must be (BusinessCategories("1","c"))
    underTest.head(4)(3) must be (BusinessCategories("1","d"))
  }

  "Hours-Array in Business Class " must "be exploded into several rows" in {
    val underTest = new Json2TableTransformer().businessHours(businesses).sort($"business_id",$"hour")
    underTest.show(false)
    underTest.count() must be (3)
    underTest.head(4)(0) must be (BusinessHours("1","x"))
    underTest.head(4)(1) must be (BusinessHours("1","y"))
    underTest.head(4)(2) must be (BusinessHours("1","z"))
  }

  /*
    checkin
   */

  "Values of time-Array in Checkin Class " must   "be converted into a comma separated String" in {
    println("input checkin raw data:")
    checkin.show(false)

    val underTest  = new Json2TableTransformer().checkinAsTable(checkin).sort($"business_id",$"time")
    println("transformed checkin relational data:")
    underTest.show(false)
    underTest.count() must be (2)
    underTest.head(2)(0).business_id must be ("1")
    underTest.head(2)(0).`type` must be ("myType")
    underTest.head(2)(0).time must be ("Wed-0:1,Tue-21:1,Sat-23:1")

    underTest.head(2)(1).business_id must be ("2")
    underTest.head(2)(1).time must be ("Tue,Sun")

  }

  "Values of time-Array in Checkin Class " must   "be exploded into several rows" in {
    println("input checkin raw data:")
    checkin.show(false)

    val underTest  = new Json2TableTransformer().checkinTimes(checkin).sort($"business_id",$"time")
    println("transformed checkin relational data:")
    underTest.show(false)
    underTest.count() must be (5)
    underTest.head(5)(0) must be (CheckinTimes("1","Sat-23:1"))
    underTest.head(5)(1) must be (CheckinTimes("1","Tue-21:1"))
    underTest.head(5)(2) must be (CheckinTimes("1","Wed-0:1"))
    underTest.head(5)(3) must be (CheckinTimes("2","Sun"))
    underTest.head(5)(4) must be (CheckinTimes("2","Tue"))
  }

  "Attribute-Array without values " must "be converted into empty string" in {


    checkin_empty.show(false)
    val underTest = new Json2TableTransformer().checkinAsTable(checkin_empty).sort($"business_id",$"time")
    underTest.show(false)
    underTest.count() must be (2)
    underTest.head(2)(0).time must be ("Wed-0:1,Tue-21:1,Sat-23:1")
    underTest.head(2)(1).time must be ("")
  }


  "Attribute-Array without values " must "be not be exploded" in {

    val underTest = new Json2TableTransformer().checkinTimes(checkin_empty).sort($"business_id",$"time")
    underTest.show(false)
    underTest.count() must be (3)
    underTest.head(5)(0) must be (CheckinTimes("1","Sat-23:1"))
    underTest.head(5)(1) must be (CheckinTimes("1","Tue-21:1"))
    underTest.head(5)(2) must be (CheckinTimes("1","Wed-0:1"))

  }

  /*
  UserEntities Tests
   */
  "Values of friends-Array in User Class " must   "be exploded into several rows" in {
    val userDS = spark.createDataset(List(testUser))
    val underTest = new Json2TableTransformer().userFriends(userDS).sort($"user_id", $"friend")
    underTest.show(false)
    underTest.count() must be(2)
    underTest.head(5)(0) must be(UserFriends("1", "friend1"))
    underTest.head(5)(1) must be(UserFriends("1", "friend2"))
  }

  "Values of elite-Array in User Class " must   "be exploded into several rows" in {
    val userDS = spark.createDataset(List(testUser))
    val underTest = new Json2TableTransformer().userElite(userDS).sort($"user_id", $"elite")
    underTest.show(false)
    underTest.count() must be(1)
    underTest.head(5)(0) must be(UserElite("1", "elite1"))
  }
}
