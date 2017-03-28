/**
  * Created by mprescha on 28.03.17.
  */

//use Array due to bug with List() (solved in Spark 2.2.0)
case class Business (
  business_id: String,
  name: String,
  neighborhood: String,
  address: String,
  city: String,
  state: Option[String],
  postal_code: String,
  latitude: Double,
  longitude: Double,
  stars: Double,
  review_count: Long,
  is_open: Long,
  attributes: Array[String],
  categories: Array[String],
  hours: Array[String],
  `type`: String
)

case class BusinessTable (
  business_id: String,
  name: String,
  neighborhood: String,
  address: String,
  city: String,
  state: Option[String],
  postal_code: String,
  latitude: Double,
  longitude: Double,
  stars: Double,
  review_count: Long,
  is_open: Long,
  attributes: String,
  categories: String,
  hours: String,
  `type`: String
)

