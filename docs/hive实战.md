1. 创建表
    ```text
    create table helloworld(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    ```
2. 加载数据：load data local inpath '/home/hadoop/data/helloworld.txt' overwrite into table helloworld ;
3. set hive.cli.print.current.db = true ;
4. 清屏：!clear ;
5. desc formatted helloworld ;
6. 创建员工表
    ```text
    create table emp(
     empno int, 
     ename string,
     job string,
     mgr int,
     hiredate string,
     sal double,
     comm double,
     deptno int
    ) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    ```
7. 加载数据：LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp ;
8. TRUNCATE TABLE emp ;