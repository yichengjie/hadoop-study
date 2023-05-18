package com.yicj.spark.hello;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.concat;
import static org.apache.spark.sql.functions.lit;

/**
 * @author: yicj
 * @date: 2023/5/16 21:31
 */
public class HelloWorldApp {

    //https://github.com/jgperrin/net.jgp.books.spark.ch01
    public static void main(String[] args) {
        // Creates a session on a local master
        SparkSession spark = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local")
                .getOrCreate();
        // Reads a CSV file with header, called books.csv, stores it in a
        // dataframe
        Dataset<Row> df = spark.read()
                .format("csv")
                .option("header", "true")
                .load("data/data.csv");
        df.show(5);
    }
}
