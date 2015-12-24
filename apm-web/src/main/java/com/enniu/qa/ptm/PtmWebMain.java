package com.enniu.qa.ptm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;


/**
 * Created by fuyong on 6/30/15.
 */

@SpringBootApplication
@EntityScan(basePackages = {"org.ngrinder.model","com.enniu.qa.ptm"})
public class PtmWebMain {
    public static void main(String[] args) {
        SpringApplication.run(PtmWebMain.class, args);
    }

}
