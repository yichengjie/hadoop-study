1. 创建目录：hadoop fs -mkdir /wsinput
2. 上传文件：hadoop fs -put ~/word.txt /wcinput/word.txt
3. 查看文件：hadoop fs -cat  /wcinput/word.txt
4. 显示文件末尾的数据：hadoop fs -tail /wsinput/word.txt
5. 追加文件到已经存在的文件末尾：hadoop fs -appendToFile word.txt /wcinput/word.txt
6. 下载文件：hadoop fs -copyToLocal /wcinput/word.txt ./
7. fdfs一个路径复制到另一个路径：hadoop fs -cp /wcinput/word.txt /wcinput2
8. 删除文件：hadoop fs -rm /wcinput/word.txt
9. 统计文件大小：hadoop fs -du -s -h /wcinput
10. 提交任务：hadoop jar hello-world-1.0-SNAPSHOT-jar-with-dependencies.jar com.yicj.hello.WordCountJob /wcinput/* wcoutput
11. hadoop jar wc-jar-with-dependencies.jar /wcinput/word.txt /wcoutput
12. hadoop jar wc.jar com.yicj.hello.WordCountApp  /wcinput/word.txt /wcoutput

    