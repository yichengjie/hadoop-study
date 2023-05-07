package com.yicj.hello.wordcount;

/**
 * @author: yicj
 * @date: 2023/5/7 19:37
 */

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 自定义Reducer, 需要继承Reducer类,指定4个泛型 , 4个泛型表示两组kv对.
 *
 * 4个泛型，表示两组kv对， 一组是输入的kv ， 一组是输出的kv
 * 输入的kv对:
 * KEYIN   : Text,对应Map输出的k, 表示一个单词
 * VALUEIN ：IntWritable , 对应Map输出的v, 表示单词出现的次数
 *
 * 输出的kv对:
 * KEYOUT  : Text, 表示一个单词
 * VALUEOUT: IntWritable , 表示这个单词出现的总次数

 */
public class WordCountReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    //定义写出的v
    IntWritable outV = new IntWritable();
    /**
     * 重写Reducer中的reudce方法。
     * @param key  表示输入的key, 就是一个单词
     * @param values 表示封装了当前key对应的所有的value的一个迭代器对象.
     * @param context 负责调度Reducer运行
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //1. 迭代values,将当前key对应的所有的value累加.
        int sum = 0 ;
        for (IntWritable value : values) {
            sum += value.get() ;
        }
        //2. 写出
        //将累加后的结果sum封装到outV中
        outV.set(sum);
        context.write(key,outV);
    }
}
