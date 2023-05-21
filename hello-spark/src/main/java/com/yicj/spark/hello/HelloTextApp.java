package com.yicj.spark.hello;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author: yicj
 * @date: 2023/5/18 22:32
 */
public class HelloTextApp {

    public static void main(String[] args) throws Exception {
        String logFile = "data/SogouQ.sample.txt"; // Should be some file on your system
        SparkSession spark = SparkSession.builder()
                .appName("CSV to Dataset")
                .master("local")
                .getOrCreate();
        Dataset<String> logData = spark.read()
                .textFile(logFile)
                .cache();

        logData.createTempView("user");
//        long numAs = logData.filter((FilterFunction<String>) s -> s.contains("00717725924582846")).count();
//        long numBs = logData.filter((FilterFunction<String>) s -> s.contains("41416219018952116")).count();
//        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
        Dataset<Row> sqlDF = spark.sql("select * from user where value like '%07594220%' ");
        sqlDF.show();
        spark.stop();
    }
}
