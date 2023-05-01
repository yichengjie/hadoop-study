1. /etc/sysconfig/network中修改hostname
2. apache软件下载地址：https://archive.apache.org/dist/
3. mwget下载（wget如果下载太慢可以使用mwget下载）
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