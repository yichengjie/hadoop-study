package com.yicj.spark.hello;

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
import java.util.Map;
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
        // java实现
        countJava(sc, path);
        System.out.println("-----------------------------------");
        // java8实现
        //countJava8(sc, path);
    }


    private static void countJava(JavaSparkContext sc, String path){
        JavaRDD<String> distFile = sc.textFile(path);
        JavaRDD<String> splitRdd = distFile.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String line) throws Exception {
                return Arrays.stream(line.split(" ")).iterator();
            }
        });
        //
        JavaPairRDD<String, Integer> splitFlagRdd = splitRdd.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<>(word, 1);
            }
        });
        // reduceByKey 会将splitFlagRdd中的key相同的放在一起处理
        // 传入(x,y)中，x是上一次统计后的value，y是本次单词的value，即每一次是 x + 1
        JavaPairRDD<String, Integer> countRdd = splitFlagRdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        // 将计算后的结果保存
        countRdd.saveAsTextFile("output/wordCountJava");
    }



    private static void countJava8(JavaSparkContext sc, String path){
        JavaRDD<String> distFile = sc.textFile(path);
        distFile.flatMap((FlatMapFunction<String, String>) s -> Arrays.stream(s.split(" ")).iterator())
                .mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1))
                .reduceByKey((Function2<Integer, Integer, Integer>) Integer::sum)
                .saveAsTextFile("output/wordCountJava8");
                //.foreach((VoidFunction<Tuple2<String, Integer>>) System.out::println);
    }

}
