package com.enniu.qa.apm;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Administrator on 2015/8/12 0012.
 */
public class PasswordEncodeTest {

    @Test
    public void getEncodePassword(){
        String raw="admin";
        System.out.println(new BCryptPasswordEncoder().encode(raw));
    }
}
