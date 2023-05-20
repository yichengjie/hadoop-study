package com.yicj.spark.scala

import org.apache.spark.sql.SparkSession


object CreatingDatasetsApp {

  def main(args: Array[String]): Unit = {

    val spark:SparkSession = SparkSession.builder()
      .master("local")
      .appName("datasets app")
      .getOrCreate() ;
    import spark.implicits._//这里spark出现了爆红

//    var schemaString = "name age"
//
//    schemaString.split(" ") ;

//    val caseClassDS = Seq(Person("Andy", 32)).toDS()
//    caseClassDS.show()
//
//    var primitiveDS = Seq(1,2,3).toDS() ;
//
//    primitiveDS.map(_ +1).collect()

    var path = "data/people.json"

    spark.read.json(path).show()
    //
    println("----------------")
    //
    val peopleDS = spark.read.json(path).as[Person]
    peopleDS.show()


  }

  case class Person(name:String, age:Long)

}
