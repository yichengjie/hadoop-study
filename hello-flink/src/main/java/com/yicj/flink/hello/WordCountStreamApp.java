package com.yicj.flink.hello;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: yicj
 * @date: 2023/5/21 14:00
 */
public class WordCountStreamApp {

    // nc64 -l -p 7788
    public static void main(String[] args) throws Exception {
        //1. 创建流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2. 从socket文件流读取数据
        DataStreamSource<String> inputDataStream = env.socketTextStream("localhost", 7788);
        //3. 基于数据流转换计算
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new WordCountApp.MyFlatMapper())
                .keyBy((KeySelector<Tuple2<String, Integer>, String>) value -> value.getField(0))
                .sum(1);
        //4. 打印数据
        resultStream.print() ;
        //5. 执行任务
        env.execute() ;
    }
}
