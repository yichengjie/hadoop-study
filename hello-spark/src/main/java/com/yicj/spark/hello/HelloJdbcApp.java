package com.yicj.spark.hello;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

import static org.apache.spark.sql.functions.concat;
import static org.apache.spark.sql.functions.lit;

/**
 * @author yicj
 * @date 2023年05月17日 12:59
 */
public class HelloJdbcApp {

    public static void main(String[] args) {
        // Creates a session on a local master
        SparkSession spark = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local")
                .getOrCreate();
        // Reads a CSV file with header, called books.csv, stores it in a
        // dataframe
        Dataset<Row> df = spark.read().format("csv")
                .option("header", "true")
                .load("data/data.csv");
        Dataset<Row> ndf = df.withColumn("name",
                concat(df.col("product_code"), lit(","), df.col("amount")));
        // Shows at most 5 rows from the dataframe
        ndf.show(5);
        //
        String url = "jdbc:mysql://localhost:3306/imoocdemo?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai" ;
        String table = "tbl_order_1" ;
        final Properties properties = new Properties() ;
        properties.put("driver", "com.mysql.cj.jdbc.Driver") ;
        properties.put("user", "root") ;
        properties.put("password", "root") ;
        ndf.write()
                .mode(SaveMode.Overwrite)
                .jdbc(url, table, properties);
    }
}
