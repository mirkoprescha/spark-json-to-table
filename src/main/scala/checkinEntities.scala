/**
  * Created by mprescha on 28.03.17.
  */

case class Checkin(
                           time: Array[String],
                           business_id: String,
                           `type`: String
                         )

case class CheckinTable(
                         business_id: String,
                         time: String,
                           `type`: String
                         )
case class CheckinTimes(
                         business_id: String,
                           time: String

                         )