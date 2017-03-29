package com.mprescha.json2Table
/**
  * Created by mprescha on 29.03.17.
  */
object ArrayConverter {
  val arrayAsString = (array: Array[String])  => {
    if(Option(array).isDefined && !array.isEmpty)
      array.mkString(",")
    else
      ""
  }
}
