1. 关闭防火墙
   ```
   a. systemctl stop firewalld.service
   b. systemctl disable firewalld.service
   c. systemctl status firewalld.service
   ```
2. vi /etc/hostname修改为hadoop100
3. vi /etc/sysconfig/network-scripts/ifcfg-enp0s3 中IPADDR中的ip地址
4. vi /etc/hosts
   ```text
   192.168.99.100 hadoop100
   ```
5. 创建用户并修改密码
   ```text
   useradd hadoop
   passwd hadoop
   ```
6. 修改hadoop用户具有root权限，方便后期加sudo执行root权限命令 vi /etc/sudoers, 在%wheel下面添加一行
   ```
   hadoop ALL=(ALL)   NOPASSWD:ALL
   ```
7. 在/opt目录下创建mkdir ~/app、mkdir ~/software文件夹
10. windows的C:Windows/System32/drivers/etc/hosts中添加配置
    ```
    192.168.99.100 hadoop100
    ```
### 机器配置ssh免密登录
1. ssh localhost
2. 进入.ssh目录：cd ~/.ssh
3. 生产密匙：ssh-keygen -t rsa
4. 将公钥写入authorized_keys：cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
5. 修改权限：chmod 600 authorized_keys
### JDK环境配置
1. 卸载自带java： rpm -qa | grep -i java | xargs -n1 rpm -e --nodeps
2. 解压jdk：tar -zxvf jdk-8u212-linux-x64.tar.gz -C ~/app/
3. vi ~/.bash_profile 文件并添加内容
    ```
    #JAVA_HOME
    export JAVA_HOME=/home/hadoop/app/jdk1.8.0_212
    export PATH=$JAVA_HOME/bin:$PATH
    ```
4. source ~/.bash_profile，让新的环境变量PATH生效
### Hadoop配置
1. 下载地址：https://archive.apache.org/dist/hadoop/common hadoop-3.1.3.tar.gz
2. tar -zxvf ~/software/hadoop-3.1.3.tar.gz -C ~/app/
3. 添加环境变量 vi ~/.bash_profile,追加内容
   ```
   #HADOOP_HOME
   export HADOOP_HOME=/home/hadoop/app/hadoop-3.1.3
   export PATH=$PATH:$HADOOP_HOME/bin
   export PATH=$PATH:$HADOOP_HOME/sbin
   ```
4. 并执行source ~/.bash_profile使配置生效
5. vi ~/app/hadoop-3.1.3/etc/hadoop/hadoop-env.sh 中java home目录
   ```
   export JAVA_HOME=/home/hadoop/app/jdk1.8.0_212
   ```
6. etc/hadoop/core-site.xml
   ```xml
   <configuration>
       <property>
           <name>fs.defaultFS</name>
           <value>hdfs://hadoop100:8020</value>
       </property>
   </configuration>
   ```
7. etc/hadoop/hdfs-site.xml
   ```xml
   <configuration>
       <property>
           <name>dfs.replication</name>
           <value>1</value>
       </property>
       <property>
           <name>hadoop.tmp.dir</name>
           <value>/home/hadoop/app/tmp</value>
       </property>
   </configuration>
   ```
8. workers
   ```text
   hadoop100
   ```
9. hdfs namenode -format
10. etc/hadoop/mapred-site.xml
   ```xml
   <configuration>
       <property>
           <name>mapreduce.framework.name</name>
           <value>yarn</value>
       </property>
       <property>
           <name>mapreduce.application.classpath</name>
           <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
       </property>
   </configuration>
   ```
11. etc/hadoop/yarn-site.xml
   ```xml
   <configuration>
       <property>
           <name>yarn.nodemanager.aux-services</name>
           <value>mapreduce_shuffle</value>
       </property>
       <property>
           <name>yarn.nodemanager.env-whitelist</name>
           <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
       </property>
   </configuration>
   ```
12. 启动hadoop
```text
start-all.sh
```