package com.enniu.qa.ptm.configuration;

import com.enniu.qa.ptm.spring.RemainedPathMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by fuyong on 9/6/15.
 */
@Configuration
public class WevMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolverList){
		argumentResolverList.add(new RemainedPathMethodArgumentResolver());
	}
}
