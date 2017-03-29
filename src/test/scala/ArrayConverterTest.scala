package com.mprescha.json2Table
import org.scalatest.{MustMatchers, FlatSpec}

/**
  * Created by mprescha on 29.03.17.
  */
class ArrayConverterTest extends FlatSpec with MustMatchers {


  "Array of String " must   "be converted in comma separated String" in {
    val myArray: Array[String] = Array("1","2")

    val underTest = ArrayConverter.arrayAsString(myArray)
    underTest must be ("1,2")
  }

  "Empty Array of String " must   "be converted in empty String" in {
    val myArray: Array[String] = Array()

    val underTest = ArrayConverter.arrayAsString(myArray)
    underTest must be ("")
  }

  "Null Array of Strings " must   "be converted in empty String" in {
    val myArray: Array[String] = null

    val underTest = ArrayConverter.arrayAsString(myArray)
    underTest must be ("")
  }

}
