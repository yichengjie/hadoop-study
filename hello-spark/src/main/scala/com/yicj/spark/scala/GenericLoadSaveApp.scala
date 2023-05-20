package com.yicj.spark.scala

import org.apache.spark.sql.SparkSession

object GenericLoadSaveApp {

  def main(args: Array[String]): Unit = {
    var spark = SparkSession.builder()
      .appName("load and save app")
      .master("local")
      .getOrCreate()

    //val usersDF = spark.read.load("data/users.parquet")
    //usersDF.select("name", "favorite_color").write.save("output/namesAndFavColors.parquet")
    //
    val peopleDF = spark.read
      .format("json")
      .load("data/people.json")

    peopleDF.select("name", "age")
      .write
      .format("json")
      .save("output/namesAndAges.parquet")

  }

}
