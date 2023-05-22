package com.yicj.spark.hello;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author: yicj
 * @date: 2023/5/22 21:50
 */
public class SparkFlatMapApp {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("flatMap app");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //java实现
        flatMapJava(sc) ;
        // java8实现
        flatMapJava8(sc) ;
    }

    private static void flatMapJava8(JavaSparkContext sc) {
        JavaRDD<String> textData = sc.textFile("data/world_count.txt");
        // 输出处理前总行数
        System.out.println("before : "+textData.count()+" 行");
        // 输出处理前第一行内容
        System.out.println("first line : "+textData.first()+" 行");
        // 进行flatMap处理
        JavaRDD<String> flatMap = textData.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String content) throws Exception {
                return Arrays.stream(content.split(" ")).iterator();
            }
        });
        // 输出处理后总行数
        System.out.println("after: "+flatMap.count()+"行");
        // 输出处理后第一行数据
        System.out.println("first line "+flatMap.first()+" 行");
        // 将结果保存到flagResultJava文件夹中
        flatMap.saveAsTextFile("output/flatResultJava");
    }

    private static void flatMapJava(JavaSparkContext sc) {
        sc.textFile("data/world_count.txt")
                .flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .saveAsTextFile("output/flatResultJava8");
        
    }
}
