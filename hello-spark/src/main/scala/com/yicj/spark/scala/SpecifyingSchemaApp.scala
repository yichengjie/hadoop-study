package com.yicj.spark.scala

import org.apache.spark.sql.{Row, SparkSession}
// import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.types._

object SpecifyingSchemaApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local")
      .appName("specify schema app")
      .getOrCreate()
    var path = "data/people.txt"
    // create an rdd
    val peopleRdd = spark.sparkContext.textFile(path)
    // the schema is encoded in a string
    var schemaString = "name age"
    // generate the schema based on the string of schame
    var fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    var schema = StructType(fields)
    // convert records of the rdd(people) to Rows
    val rowRdd = peopleRdd.map(_.split(","))
      .map(attributes => Row(attributes(0), attributes(1).trim))
    //Apply the schema to the rdd
    val peopleDF = spark.createDataFrame(rowRdd, schema)
    // Create a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")
    // SQL can be run over a temporary view created using DataFrames
    var results = spark.sql("select name, age from people")
    results.show()
    //
    import spark.implicits._
    results.map(attributes => "Name : " + attributes.getAs[String]("name")).show()

    spark.stop()
  }

}
