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
      attributes= b.attributes.mkString(","),
      categories=  b.categories.mkString(","),
      hours=  b.hours.mkString(","),
      `type`=  b.`type`
    ))
    return ds

  }
}
