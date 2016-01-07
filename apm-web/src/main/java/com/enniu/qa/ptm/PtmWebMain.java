package com.enniu.qa.ptm;


import com.enniu.qa.ptm.service.PerfTestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


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
