package com.yicj.spark.hello;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author yicj
 * @date 2023年05月19日 13:12
 */
public class HelloCsvApp {

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
        df.withColumn("name",new Column("count"))
            .show(5);
    }
}
