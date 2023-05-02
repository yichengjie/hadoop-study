### yum安装方式
1. rpm -qa | grep -i mariadb
2. rpm -e --nodeps mariadb-libs-5.5.64-1.el7.x86_64
3. rpm -qa | grep mysql
4. wget https://repo.mysql.com//mysql80-community-release-el7-3.noarch.rpm
5. yum -y install mysql80-community-release-el7-3.noarch.rpm
6. rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022
7. yum -y install mysql-community-server
8. 开启mysql服务：systemctl start mysqld.service
9. 查看mysql默认密码并登录：cat /var/log/mysqld.log | grep password
10. ALTER USER USER() IDENTIFIED BY 'Admin2022!';
11. set global validate_password.policy=0;
12. set global validate_password.length=4;
13. set global validate_password.check_user_name=OFF
14. ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
15. use mysql ;
16. update user set host = '%' where user = 'root';
17. flush privileges;
18. update user set plugin = 'mysql_native_password' where user = 'root';
19. flush privileges;

