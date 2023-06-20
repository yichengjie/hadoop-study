package com.yicj.spark.hello;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class PiEstimationApp {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("pi estimation app")
                .master("local[1]")
                .getOrCreate() ;

        JavaSparkContext sc = JavaSparkContext.fromSparkContext(spark.sparkContext());

        int NUM_SAMPLES = 100 ;
        List<Integer> l = new ArrayList<>(NUM_SAMPLES);
        for (int i = 0; i < NUM_SAMPLES; i++) {
            l.add(i);
        }

        long count = sc.parallelize(l).filter(i -> {
            double x = Math.random();
            double y = Math.random();
            return x*x + y*y < 1;
        }).count();
        System.out.println("Pi is roughly " + 4.0 * count / NUM_SAMPLES);
    }
}
