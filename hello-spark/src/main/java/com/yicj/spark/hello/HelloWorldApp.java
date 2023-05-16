package com.yicj.spark.hello;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author: yicj
 * @date: 2023/5/16 21:31
 */
public class HelloWorldApp {

    //https://github.com/jgperrin/net.jgp.books.spark.ch01
    public static void main(String[] args) {
        HelloWorldApp app = new HelloWorldApp() ;
        app.start();
    }

    /**
     * The processing code.
     */
    private void start() {
        // Creates a session on a local master
        SparkSession spark = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local")
                .getOrCreate();
        // Reads a CSV file with header, called books.csv, stores it in a
        // dataframe
        Dataset<Row> df = spark.read().format("csv")
                .option("header", "true")
                .load("D:\\opt\\spark\\helloworld\\input\\data.csv");
        // Shows at most 5 rows from the dataframe
        df.show(5);
    }
}
