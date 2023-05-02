1. 创建表
    ```text
    create table helloworld(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    ```
2. 加载数据：load data local inpath '/home/hadoop/data/helloworld.txt' overwrite into table helloworld ;
3. set hive.cli.print.current.db = true ;
4. 清屏：!clear ;
5. desc formatted helloworld ;
#### 部门员工数据
1. use test_db ;
2. TRUNCATE TABLE emp ;
3. 创建员工表
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
4. 加载数据：LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp ;
5. 创建部门表:
    ```text
    create table dept(
        deptno string, 
        dname string, 
        loc string
    ) row format delimited fields terminated by '\t';
    ```
6. 导入部门表数据: LOAD DATA LOCAL INPATH '/home/hadoop/data/dept.txt' OVERWRITE INTO TABLE dept ;
7. select a.empno, a.ename, a.job, b.dname from emp a, dept b where a.deptno = b.deptno ;
8. select a.deptno, count(1) from emp a, dept b where a.deptno = b.deptno group by a.deptno ;
9. insert into dept values (50, 'test', 'test') ;
10. delete from dept where deptno = '50' ;
#### 行转列
1. 创建表
   ```text
   create table emp_info(
       id string,
       name string,
       dept string,
       sex string
   ) row format delimited fields terminated by ',';
   ```
2. 加载数据: load data local inpath '/home/hadoop/data/emp_info.txt' OVERWRITE INTO TABLE emp_info ;
3. 将部门与性别拼接在一起：
   ```sql 
   select concat_ws(',',a.dept,a.sex) dept_sex , a.name from emp_info a  ;
   ```
4. 子查询
   ```sql
   select t.dept_sex, concat_ws('|',collect_set(t.name)) 
   from (
       select name, concat_ws(',', dept, sex) dept_sex from emp_info
   ) t group by t.dept_sex ;
   ```
#### 博客
1. https://blog.csdn.net/qq_41575918/article/details/127755508
