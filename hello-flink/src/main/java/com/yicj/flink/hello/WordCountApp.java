package com.yicj.flink.hello;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author: yicj
 * @date: 2023/5/21 13:40
 */
// 1. 获取上下文环境，table的环境
// 2. 读物score.csv
// 3. 注册成内存表
// 4. 编写sql然后提交执行
// 5. 结果进行打印
public class WordCountApp {

    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 2. 加载数据
        // 从文件中读取数据
        String path = "data/word.txt" ;
        DataSource<String> inputDataSet = env.readTextFile(path);
        // 3. 对数据集进行处理，按空格展开，转换成(word,1)的二元组，进行统计
        AggregateOperator<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap(new MyFlatMapper())
                .groupBy(0) // 按照第一个位置word分组
                .sum(1);// 将第二个位置的数据求和
        // 4. 结果打印
        resultSet.print();
    }


    static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>>{
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word : value.split(" ")) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}
