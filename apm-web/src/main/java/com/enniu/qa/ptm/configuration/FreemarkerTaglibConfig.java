package com.enniu.qa.ptm.configuration;

import com.enniu.qa.ptm.configuration.ClassPathTldsLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fuyong on 8/20/15.
 */
@Configuration
public class FreemarkerTaglibConfig {
	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader(){
		return new ClassPathTldsLoader();
	}

}
