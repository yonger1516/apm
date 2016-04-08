package com.enniu.qa.apm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;


/**
 * Created by fuyong on 6/30/15.
 */

@SpringBootApplication
@EntityScan(basePackages = {"org.ngrinder.model","com.enniu.qa.apm"})
public class PtmWebMain {

    public static void main(String[] args) {
        SpringApplication.run(PtmWebMain.class, args);
    }

}
