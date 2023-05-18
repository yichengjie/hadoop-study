package com.yicj.spark.sql;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.math.Ordering;

import static org.apache.spark.sql.functions.col;

/**
 * @author: yicj
 * @date: 2023/5/18 21:55
 */
public class HelloSqlApp {

    //https://gitcode.net/mirrors/apache/spark?utm_source=csdn_github_accelerator
    //D:\opt\spark\helloworld\input\SogouQ.sample.txt
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local")
                .getOrCreate();
        Dataset<Row> df = spark.read().json("data/people.json");
        df.show();
        df.printSchema();
        df.select(col("name"), col("age").plus(1)).show();
        df.filter(col("age").gt(21)).show();
        df.groupBy("age").count().show();
        spark.close();
    }
}
