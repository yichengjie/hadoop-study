package com.yicj.hello;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author: yicj
 * @date: 2023/4/29 20:02
 */
public class WordCountApp {
    static Logger logger = LoggerFactory.getLogger(WordCountApp.class) ;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        logger.info("input arg[0] : {}", args[0]);
        logger.info("input arg[1] : {}", args[1]);
        //1. 创建一个Job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //2. 关联jar
        job.setJarByClass(WordCountApp.class);
        //3. 关联Mapper 和 Reducer 类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        //4. 设置Mapper的输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //5. 设置最终输出的key和value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //6. 设置输入和输出路径
        //FileInputFormat.setInputPaths(job,new Path("F:/input/inputWord.txt"));
        // FileOutputFormat.setOutputPath(job,new Path("F:/output")); // 输出路径不能存在，如果已经存在就报异常.
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        //7. 提交job
        System.exit(job.waitForCompletion(true) ? 0 :1) ;
    }


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
    public static class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

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
    public static class WordCountReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
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
}
