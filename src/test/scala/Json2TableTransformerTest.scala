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

  "Business Class " must   "be converted in a Business Table" in {
    val df = spark.read.json("/data/home/mprescha/git/spark-json-to-table/src/test/resources/business.json")
    df.printSchema()
    df.show(false)
  }

  "Arrays of Business Class " must   "be converted in String" in {
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
    val underTest = new Json2TableTransformer().businessHours(businesses).sort($"business_id",$"hours")
    underTest.show(false)
    underTest.count() must be (3)
    underTest.head(4)(0) must be (BusinessHours("1","x"))
    underTest.head(4)(1) must be (BusinessHours("1","y"))
    underTest.head(4)(2) must be (BusinessHours("1","z"))
  }
}
