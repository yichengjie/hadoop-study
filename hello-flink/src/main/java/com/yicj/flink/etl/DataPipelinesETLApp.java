package com.yicj.flink.etl;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author: yicj
 * @date: 2023/5/27 21:02
 */
public class DataPipelinesETLApp {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KeyedStream<String, String> control =
                env.fromElements("DROP", "IGNORE")
                .keyBy(x -> x);
        //
        KeyedStream<String, String> streamOfWords =
                env.fromElements("Apache", "DROP", "IGNORE")
                .keyBy(x -> x);
        //
        control.connect(streamOfWords)
                .flatMap(new ControlFunction())
                .print() ;

        env.execute() ;
    }

    public static class ControlFunction extends RichCoFlatMapFunction<String,String, String>{

        private ValueState<Boolean> blocked ;

        @Override
        public void open(Configuration parameters) throws Exception {
            blocked = getRuntimeContext()
                    .getState(new ValueStateDescriptor<Boolean>("block", Boolean.class)) ;
        }

        @Override
        public void flatMap1(String value, Collector<String> out) throws Exception {
            blocked.update(Boolean.TRUE);
        }

        @Override
        public void flatMap2(String value, Collector<String> out) throws Exception {
            if (blocked.value() == null){
                out.collect(value);
            }
        }
    }
}
