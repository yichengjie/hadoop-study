package com.yicj.spark.hello;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
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
        String path = "data/README.md" ;
        SparkSession spark = SparkSession.builder()
                .appName("readme app")
                .master("local[1]")
                .getOrCreate();

        Dataset<String> df = spark.read()
                .textFile(path);
        long count = df.count();
        //
        String first = df.first();
        //
        df.filter((FilterFunction<String>)line -> line.contains("Spark")) ;
        //
        long countSpark = df.filter((FilterFunction<String>) line -> line.contains("Spark"))
                .count();
        //
//        df.map(new MapFunction<String, Integer>() {
//            @Override
//            public Integer call(String value) throws Exception {
//                return null;
//            }
//        });
    }
}
