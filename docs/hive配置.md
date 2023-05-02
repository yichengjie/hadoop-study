1. cp hive-env.sh.template hive-env.sh
2. vi vi hive-env.sh
    ```text
    HADOOP_HOME=/home/hadoop/app/hadoop-3.1.3
    export HIVE_CONF_DIR=/home/hadoop/app/hive-3.1.3/conf
    ```
3. mysql的jar驱动包上传至~/app/hive-3.1.3/lib中
4. $HIVE_HOME/conf 修改hive-site.xml
    ```xml
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <property>
            <name>javax.jdo.option.ConnectionURL</name>
            <value>jdbc:mysql://192.168.99.51:3306/hive?createDatabaseIfNotExist=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=Asia/Shanghai</value>
        </property>
        <property>
            <name>javax.jdo.option.ConnectionDriverName</name>
            <value>com.mysql.cj.jdbc.Driver</value>
        </property>
        <property>
            <name>javax.jdo.option.ConnectionUserName</name>
            <value>root</value>
        </property>
        <property>
            <name>javax.jdo.option.ConnectionPassword</name>
            <value>root</value>
        </property>
        <property>
            <name>hive.metastore.schema.verification</name>
            <value>false</value>
        </property>
        <property>
             <name>hive.metastore.event.db.notification.api.auth</name>
             <value>false</value>
        </property>
    </configuration>
    ```
5. 复制mysql-connector-java-8.0.25.jar 到hive-3.1.3/lib中
6. 删除hive-3.1.3/lib/guava-19.0.jar
7. 将hadoop-3.1.3/share/hadoop/common/lib中的guava-27.0-jre.jar复制到hive-3.1.3/lib中
8. 格式化hive: schematool -dbType mysql -initSchema
9. 验证hive: hive