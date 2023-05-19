package com.yicj.spark.textfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Int;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yicj
 * @date 2023年05月19日 13:28
 */
@Slf4j
public class WordCountApp {

    public static void main(String[] args) {
        String appName = "rdd app";
        String master = "local";
        String path = "data/world_count.txt";
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> distFile = sc.textFile(path);
//        JavaPairRDD<String, Integer> countRdd = distFile.mapToPair(s -> new Tuple2<>(s, 1))
//                .reduceByKey((a, b) -> a + b);
//        //countRdd.foreach(System.out::println);
//        List<Tuple2<String, Integer>> collect = countRdd.collect();
//        collect.forEach(tuple -> System.out.println(tuple));
        distFile.flatMap((FlatMapFunction<String, String>) s -> Arrays.stream(s.split(" ")).collect(Collectors.toList()).iterator())
                .mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1))
                .reduceByKey((Function2<Integer, Integer, Integer>) Integer::sum)
                .foreach((VoidFunction<Tuple2<String, Integer>>) System.out::println);
    }

}
