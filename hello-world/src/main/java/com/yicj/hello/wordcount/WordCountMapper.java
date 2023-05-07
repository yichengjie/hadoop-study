package com.yicj.hello.wordcount;

/**
 * @author: yicj
 * @date: 2023/5/7 19:36
 */

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 自定义Mapper:  继承Mapper类, 指定4个泛型.  4个泛型表示两组kv对.
 *
 * 4个泛型 表示两组kv对， 一组是输入数据的kv  ，一组是输出数据的kv
 *
 * 输入数据的kv:
 * KEYIN   :  LongWritable , 表示从文件中读取数据的偏移量. (读取到了什么位置，下一次从哪个位置读取)
 * VALUEIN :  Text , 表示从文件中读取的一行数据.
 *
 * 输出数据的kv:
 * KEYOUT  :  Text, 表示一个单词
 * VALUEOUT:  IntWritable , 表示单词出现了1次.
 */
public class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    //定义输出的v
    IntWritable outV = new IntWritable(1);

    //定义输出的k
    Text outK = new Text();

    /**
     * 重写Mapper类中的map方法
     * @param key   表示输入的k
     * @param value 表示输入的v ，就是文件中读取的一行内容
     * @param context  负责调度Mapper运行
     */
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1. 将输入的一行数据，转换成String类型.
        //  atguigu atguigu
        String line = value.toString();
        //2. 使用空格切分数据
        String[] splits = line.split(" ");
        //3. 迭代splits数组，将每个单词处理成kv，写出去.
        for (String word : splits) {
            //将当前单词设置到outK中
            outK.set(word);
            //写出
            context.write(outK,outV);
        }
    }
}