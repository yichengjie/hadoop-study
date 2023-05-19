package com.yicj.spark.textfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author yicj
 * @date 2023年05月19日 13:28
 */
@Slf4j
public class WordCountApp {

    public static void main(String[] args) {
        String appName = "rdd app" ;
        String master = "local" ;
        String path = "data/world_count.txt" ;
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> distFile = sc.textFile(path);
        JavaRDD<Integer> lineLengths = distFile.map(s -> s.split(" ").length);
        int totalWorld = lineLengths.reduce((a, b) -> a + b);
        log.info("--------> {}", totalWorld);
        sc.close();
    }
}
