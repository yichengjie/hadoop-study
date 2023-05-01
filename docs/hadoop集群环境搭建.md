### 基础环境准备
1. 关闭防火墙
   ```
   a. systemctl stop firewalld.service
   b. systemctl disable firewalld.service
   c. systemctl status firewalld.service
   ```
2. vi /etc/hostname三个机器各自修改为hadoop101、hadoop102、hadoop103
3. vi /etc/sysconfig/network中修改hostname # 这个centos7中不存在
4. vi /etc/sysconfig/network-scripts/ifcfg-enp0s3 中IPADDR中的ip地址
5. vi /etc/hosts
   ```text
   192.168.99.101 hadoop101
   192.168.99.102 hadoop102
   192.168.99.103 hadppo103
   ```
6. 创建用户并修改密码
   ```text
   useradd hadoop
   passwd hadoop
   ```
7. 修改hadoop用户具有root权限，方便后期加sudo执行root权限命令 vi /etc/sudoers, 在%wheel下面添加一行
   ```
   hadoop ALL=(ALL)   NOPASSWD:ALL
   ```
8. 在/opt目录下创建mkdir /opt/module、mkdir /opt/software文件夹
9. 修改module、software文件夹的所有者和所属组均为hadoop用户
   ```
   chown -R hadoop:hadoop /opt/module
   chown -R hadoop:hadoop /opt/software
   ```
10. windows的C:Windows/System32/drivers/etc/hosts中添加配置
    ```
    192.168.99.101 hadoop101
    192.168.99.102 hadoop102
    192.168.99.103 hadppo103
    ```
### 机器配置ssh免密登录
1. ssh localhost
2. 进入.ssh目录：cd ~/.ssh
3. 生产密匙：ssh-keygen -t rsa
4. 分发密钥：ssh-copy-id hadoop102, ssh-copy-id hadoop103
### JDK环境配置
1. 卸载自带java： rpm -qa | grep -i java | xargs -n1 rpm -e --nodeps
2. 解压jdk：tar -zxvf jdk-8u212-linux-x64.tar.gz -C /opt/module/
3. vi /etc/profile.d/my_env.sh文件并添加内容
    ```
    #JAVA_HOME
    export JAVA_HOME=/opt/module/jdk1.8.0_212
    export PATH=$PATH:$JAVA_HOME/bin
    ```
4. source /etc/profile，让新的环境变量PATH生效
5. 重启机器: reboot
### Hadoop配置
1. 下载地址：https://archive.apache.org/dist/hadoop/common hadoop-3.1.3.tar.gz
2. tar -zxvf hadoop-3.1.3.tar.gz -C /opt/module/
3. 添加环境变量 vi /etc/profile.d/my_env.sh,追加内容，并执行source /etc/profile使配置生效
   ```
   #HADOOP_HOME
   export HADOOP_HOME=/opt/module/hadoop-3.1.3
   export PATH=$PATH:$HADOOP_HOME/bin
   export PATH=$PATH:$HADOOP_HOME/sbin
   ```
4. 配置hadoop-3.1.3/etc/hadoop/hadoop-env.sh中java home目录
   ```
   JAVA_HOME=/opt/module/jdk1.8.0_212
   ```
5. 编辑hadoop-3.1.3/etc/hadoop/workers
   ```
   hadoop101
   hadoop102
   hadoop103
   ```
6. 修改hadoop-3.1.3/etc/hadoop/core-site.xml
7. 修改core-site.xml、hdfs-site.xml、mapred-site.xml、yarn-site.xml （参见后面详情部分）
8. namenode节点hdfs格式化：./bin/hdfs namenode -format
9. 将hadoop复制到hadoop102,hadoop103机器
   ```
   scp -r /opt/module/hadoop-3.1.3  hadoop@192.168.99.102:/opt/modules/hadoop-3.1.3
   ```
### Hadoop运行
1. hadoop101节点启动: start-dfs.sh
2. hadoop102节点启动: mapred --daemon start historyserver
3. hadoop103节点启动: start-yarn.sh
4. jps查看进程：
   ```
   hadoop101有 NameNode和SecondNameNode
   hadoop102和hadoop103有DataNode
   hadoop102有ResourceManager和NodeManager
   ```
4. 访问nodenode：http://hadoop101:9870
5. 访问yarn: http://hadoop102:19888
### 其他补充
1. mwget下载（wget如果下载太慢可以使用mwget下载）
   ```text
   wget http://jaist.dl.sourceforge.net/project/kmphpfm/mwget/0.1/mwget_0.1.0.orig.tar.bz2
   yum install bzip2 gcc-c++ openssl-devel intltool -y
   bzip2 -d mwget_0.1.0.orig.tar.bz2
   tar -xvf mwget_0.1.0.orig.tar 
   cd mwget_0.1.0.orig
   ./configure
   make
   make install
   ```
2. 按住向右方向键 + ctrl可退出virtualbox鼠标界面
### 配置详情
1. core-site.xml
   ```xml
   <configuration>
       <!-- 指定NameNode的地址 -->
       <property>
           <name>fs.defaultFS</name>
           <value>hdfs://hadoop101:8020</value>
       </property>
       <!-- 指定hadoop数据的存储目录 -->
       <property>
           <name>hadoop.tmp.dir</name>
           <value>/opt/module/hadoop-3.1.3/data</value>
       </property>
       <!-- 配置HDFS网页登录使用的静态用户为hadoop -->
       <property>
           <name>hadoop.http.staticuser.user</name>
           <value>hadoop</value>
       </property>
   </configuration>
   ```
2. hdfs-site.xml
   ```xml
   <configuration>
       <!-- NameNode web端访问地址-->
       <property>
           <name>dfs.namenode.http-address</name>
           <value>hadoop101:9870</value>
       </property>
   	<!-- SecondNameNode web端访问地址-->
       <property>
           <name>dfs.namenode.secondary.http-address</name>
           <value>hadoop101:9868</value>
       </property>
   </configuration>
   ```
3. mapred-site.xml
   ```xml
   <configuration>
      <!-- 指定MapReduce程序运行在Yarn上 -->
       <property>
           <name>mapreduce.framework.name</name>
           <value>yarn</value>
       </property>
       <!-- 历史服务器端地址 -->
       <property>
           <name>mapreduce.jobhistory.address</name>
           <value>hadoop102:10020</value>
       </property>
       <!-- 历史服务器web端地址 -->
       <property>
           <name>mapreduce.jobhistory.webapp.address</name>
           <value>hadoop102:19888</value>
       </property>
   </configuration>
   ```
4. yarn-site.xml
   ```xml
   <configuration>
       <!-- 指定MR走shuffle -->
       <property>
           <name>yarn.nodemanager.aux-services</name>
           <value>mapreduce_shuffle</value>
       </property>
       <!-- 指定ResourceManager的地址-->
       <property>
           <name>yarn.resourcemanager.hostname</name>
           <value>hadoop103</value>
       </property>
       <!-- 环境变量的继承 -->
       <property>
           <name>yarn.nodemanager.env-whitelist</name>
         <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
       </property>
       <!-- 开启日志聚集功能 -->
       <property>
           <name>yarn.log-aggregation-enable</name>
           <value>true</value>
       </property>
       <!-- 设置日志聚集服务器地址 -->
       <property>  
           <name>yarn.log.server.url</name>  
           <value>http://hadoop102:19888/jobhistory/logs</value>
       </property>
       <!-- 设置日志保留时间为7天 -->
       <property>
           <name>yarn.log-aggregation.retain-seconds</name>
           <value>604800</value>
       </property>
   </configuration>
   ```