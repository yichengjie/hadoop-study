package com.yicj.hello.wordcount;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author: yicj
 * @date: 2023/4/29 20:02
 */
public class WordCountApp {
    static Logger logger = LoggerFactory.getLogger(WordCountApp.class) ;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //logger.info("input arg[0] : {}", args[0]);
        //logger.info("input arg[1] : {}", args[1]);
        //1. 创建一个Job对象
        Configuration conf = WordCountApp.initConfiguration();
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
        job.setNumReduceTasks(1);
        FileInputFormat.setInputPaths(job,new Path("data/word.txt"));
        FileOutputFormat.setOutputPath(job,new Path("output/wordcount/output")); // 输出路径不能存在，如果已经存在就报异常.
        //FileInputFormat.setInputPaths(job,new Path(args[0]));
        //FileOutputFormat.setOutputPath(job,new Path(args[1]));
        //7. 提交job
        System.exit(job.waitForCompletion(true) ? 0 :1) ;
    }



    public static Configuration initConfiguration(){
        Configuration conf = new Configuration();

        return conf ;
    }
}
