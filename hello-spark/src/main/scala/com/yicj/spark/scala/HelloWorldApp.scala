package com.yicj.spark.scala

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

object HelloWorldApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Spark Sql basic example")
      .master("local")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    var df = spark.read.json("data/people.json")
//    df.show() ;
//
//    df.printSchema()
//
//    df.select("name").show() ;
    //
    // df.filter(col("age") > 21).show()
    //df.groupBy("age").count().show()

   // df.createTempView("people")

//    var sqlDF = spark.sql("select * from people")
//    sqlDF.show()

    import spark.implicits._//这里spark出现了爆红

    df.select($"name", $"age" +1).show()

//    df.createGlobalTempView("people")
//    spark.sql("select * from global_temp.people").show()
//    //
//    spark.newSession().sql("select * from global_temp.people").show()

    spark.close()
  }

}
