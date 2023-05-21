package com.yicj.flink.hello;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author: yicj
 * @date: 2023/5/20 16:07
 */
public class WordUpperCaseApp {

    public static void main(String[] args) throws Exception {
        // 1.准备环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        // 2.加载数据源
        DataStreamSource<String> elementsSource = env.fromElements(
                "java,scala,php,c++", "java,scala,php", "java,scala", "java");
        //3. 数据转化
        SingleOutputStreamOperator<String> flatMap = elementsSource.flatMap(new MyFlatMapper());
        // DataStream下边为DataStream的子类
        SingleOutputStreamOperator<String> source = flatMap.map((MapFunction<String, String>) String::toUpperCase);
        //4. 数据输出
        source.print() ;
        //5. 执行程序
        env.execute() ;
    }

    static class MyFlatMapper implements FlatMapFunction<String, String>{

        @Override
        public void flatMap(String value, Collector<String> out) throws Exception {
            String[] wordArr = value.split(",");
            for (String word : wordArr) {
                out.collect(word);
            }
        }
    }
}
