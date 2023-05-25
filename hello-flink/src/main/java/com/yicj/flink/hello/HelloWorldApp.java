package com.yicj.flink.hello;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: yicj
 * @date: 2023/5/25 21:42
 */
public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.read
        DataStreamSource<Person> flintstones = env.fromElements(
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2)
        );
        SingleOutputStreamOperator<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person value) throws Exception {
                return value.getAge() >= 18;
            }
        });
        adults.print() ;
        env.execute() ;
    }

    @Getter
    @Setter
    public static class Person {
        public String name;
        public Integer age;
        public Person() {}

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return this.name.toString() + ": age " + this.age.toString();
        }
    }
}
