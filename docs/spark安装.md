1. 下载地址：https://spark.apache.org/downloads.html, https://mirrors.bfsu.edu.cn/apache/
2. 安装笔记：https://blog.csdn.net/m0_70885101/article/details/127641702
3. spark-env.sh配置
    ```text
    export JAVA_HOME=/home/hadoop/app/jdk1.8.0_212
    export HADOOP_HOME=/home/hadoop/app/hadoop-3.1.3
    export HADOOP_CONF_DIR=/home/hadoop/app/hadoop-3.1.3/etc/hadoop
    export SCALA_HOME=/home/hadoop/app/scala-2.13.10
    export SPARK_HOME=/home/hadoop/app/spark-3.2.4-bin-hadoop3.2
    export SPARK_MASTER_IP=192.168.99.100
    export SPARK_MASTER_PORT=7077
    export SPARK_MASTER_WEBUI_PORT=8080
    export SPARK_WORKER_CORES=3
    export SPARK_WORKER_INSTANCES=1
    export SPARK_WORKER_MEMORY=5G
    export SPARK_WORKER_WEBUI_PORT=8081
    export SPARK_EXECUTOR_CORES=1
    export SPARK_EXECUTOR_MEMORY=1G
    export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:$HADOOP_HOME/lib/native
    ```
4. slaves配置
    ```text
    localhost
    ```
5. 启动master: start-master.sh
6. 启动worker: start-worker.sh spark://hadoop100:7077 --webui-port 8081 
7. 验证：./bin/run-example SparkPi