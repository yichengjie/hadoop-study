1. /etc/sysconfig/network中修改hostname
2. apache软件下载地址：https://mirrors.bfsu.edu.cn/apache/
3. mwget下载（wget如果下载太慢可以使用mwget下载）
   ```text
   yum install bzip2 gcc-c++ openssl-devel intltool -y
   wget https://files.cnblogs.com/files/firewalld/mwget_0.1.0.orig.tar.gz -O /tmp/mwget_0.1.0.orig.tar.gz
   tar -xf /tmp/mwget_0.1.0.orig.tar.gz -C /tmp && cd /tmp/mwget_0.1.0.orig
   ./configure && make -j4 && make install
   mwget -v
   ```
4. spark elt: https://blog.csdn.net/weixin_46117281/article/details/121978605
5. nc64 -l -p 7788