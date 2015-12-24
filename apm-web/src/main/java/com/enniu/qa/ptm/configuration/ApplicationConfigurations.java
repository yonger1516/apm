package com.enniu.qa.ptm.configuration;

import com.enniu.qa.ptm.service.AgentManagerService;
import com.enniu.qa.ptm.service.ClusteredAgentManagerService;
import com.enniu.qa.ptm.spring.ApiExceptionHandlerResolver;
import net.grinder.engine.agent.LocalScriptTestDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfigurations {

    @Autowired
    Config config;

    @Bean
    public LocalScriptTestDriveService getLocalScriptTestDriveService(){
        return new LocalScriptTestDriveService();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TaskScheduler getTaskScheduler(){
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public ApiExceptionHandlerResolver apiExceptionHandlerResolver(){
        return new ApiExceptionHandlerResolver();
    }

    @Bean
    public AgentManagerService agentManagerService(){
        if (config.isClustered()){
            return new ClusteredAgentManagerService();
        }else{
            return new AgentManagerService();
        }
    }
}