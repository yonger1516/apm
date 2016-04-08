package com.enniu.qa.apm.configuration;

import com.enniu.qa.apm.dao.hbase.ApplicationFactory;
import com.enniu.qa.apm.dao.hbase.DefaultApplicationFactory;
import com.enniu.qa.apm.service.AgentManagerService;
import com.enniu.qa.apm.service.ClusteredAgentManagerService;
import com.enniu.qa.apm.spring.ApiExceptionHandlerResolver;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.hbase.HbaseTemplate2;
import com.navercorp.pinpoint.common.hbase.PooledHTableFactory;
import com.navercorp.pinpoint.common.service.DefaultServiceTypeRegistryService;
import com.navercorp.pinpoint.common.service.DefaultTraceMetadataLoaderService;
import com.navercorp.pinpoint.common.service.ServiceTypeRegistryService;
import net.grinder.engine.agent.LocalScriptTestDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseConfigurationFactoryBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

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

    @Autowired
    HBaseConfiguration hBaseConfiguration;

    @Bean
    public HbaseOperations2 getHbaseOperations2(){

        HbaseConfigurationFactoryBean hbaseConfigurationFactoryBean=new HbaseConfigurationFactoryBean();

        org.apache.hadoop.conf.Configuration configuration= org.apache.hadoop.hbase.HBaseConfiguration.create();

        configuration.set("hbase.zookeeper.quorum", hBaseConfiguration.getHost());
        configuration.setInt("hbase.zookeeper.property.clientPort", hBaseConfiguration.getPort());
        configuration.setBoolean("hbase.ipc.client.tcpnodelay", hBaseConfiguration.isTcpNoDelay());
        configuration.setLong("hbase.rpc.timeout", hBaseConfiguration.getRpcTimeOut());
        configuration.setLong("hbase.ipc.client.socket.timeout.read", hBaseConfiguration.getReadTimeOut());
        configuration.setLong("hbase.ipc.client.socket.timeout.write", hBaseConfiguration.getWriteTimeOut());
        configuration.setLong("hbase.client.operation.timeout", hBaseConfiguration.getOpsTimeOut());

        hbaseConfigurationFactoryBean.setConfiguration(configuration);
        hbaseConfigurationFactoryBean.afterPropertiesSet();

        HbaseTemplate2 template2=new HbaseTemplate2();
        template2.setConfiguration(hbaseConfigurationFactoryBean.getObject());
        PooledHTableFactory tableFactory=new PooledHTableFactory(configuration,hBaseConfiguration.getMaxThreads(),hBaseConfiguration.getQueueSize(),hBaseConfiguration.isPreStart());

        template2.setTableFactory(tableFactory);
        template2.afterPropertiesSet();

        return template2;
    }

    @Bean
    public ServiceTypeRegistryService getServiceTypeRegistryService(){
        return new DefaultServiceTypeRegistryService(new DefaultTraceMetadataLoaderService());
    }

   /* @Bean
    public ApplicationFactory getApplicationFactory(){
        return new DefaultApplicationFactory();
    }*/


}