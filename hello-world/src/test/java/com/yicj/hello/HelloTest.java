package com.yicj.hello;

import org.junit.Test;

/**
 * @author: yicj
 * @date: 2023/4/30 10:39
 */
public class HelloTest {

    @Test
    public void hello(){
        String content = "127.0.0.1 - - [15/Feb/2020:11:57:42 +0800] \"GET / HTTP/1.1\" 200 1624 \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362\"" ;
        String[] infos = content.split(" ");
        for (String info: infos){
            System.out.println(info);
        }
    }
}
