package com.yicj.spark.scala

import com.yicj.spark.scala.CreatingDatasetsApp.Person
import org.apache.spark.sql.SparkSession

object InteroperationRddsApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("rdds app")
      .master("local")
      .getOrCreate();

    import spark.implicits._
    // create an rdd of person objects from a text file, convert it to a Dataframe
    var path = "data/people.txt"
    val peopleDF = spark.sparkContext
      .textFile(path)
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDF();
    // register the dataFrame as temporary view
    peopleDF.createOrReplaceTempView("people")
    //
    //sql statements can be run by using the sql method provide by spark
    var teenagersDF = spark.sql("select name, age from people where age between 13 and 19")

    // accessed by field index
    teenagersDF.map(teenager => "Name : " + teenager(0)).show()
    // accessed by field name
    teenagersDF.map(teenager => "Name : " + teenager.getAs[String]("name")).show()

    //
    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]

    val maps = teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()

    println(maps)

    spark.stop()
  }

}
