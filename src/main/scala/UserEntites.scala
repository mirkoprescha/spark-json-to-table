package com.mprescha.json2Table
/**
  * Created by mprescha on 29.03.17.
  */

case class User(
                           user_id: String,
                           name: String,
                           review_count: Double,
                           yelping_since: String,
                           friends: Array[String],
                           useful: Double,
                           funny: Double,
                           cool: Double,
                           fans: Double,
                           elite: Array[String],
                           average_stars: Double,
                           compliment_hot: Double,
                           compliment_more: Double,
                           compliment_profile: Double,
                           compliment_cute: Double,
                           compliment_list: Double,
                           compliment_note: Double,
                           compliment_plain: Double,
                           compliment_cool: Double,
                           compliment_funny: Double,
                           compliment_writer: Double,
                           compliment_photos: Double,
                           `type`: String
                         )

case class UserFriends(
                        user_id: String,
                        friend: String

                       )

case class UserElite(
                        user_id: String,
                        elite: String
                      )