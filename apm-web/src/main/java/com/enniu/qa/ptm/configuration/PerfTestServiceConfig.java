
package com.enniu.qa.ptm.configuration;

import com.enniu.qa.ptm.dao.PerfTestRepository;
import com.enniu.qa.ptm.handler.ScriptHandlerFactory;
import com.enniu.qa.ptm.model.AgentManager;
import com.enniu.qa.ptm.service.ClusteredPerfTestService;
import com.enniu.qa.ptm.service.ConsoleManager;
import com.enniu.qa.ptm.service.FileEntryService;
import com.enniu.qa.ptm.service.PerfTestService;
import org.ngrinder.service.AbstractPerfTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
public class PerfTestServiceConfig  {

	@Autowired
	private PerfTestRepository perfTestRepository;

	@Autowired
	private ConsoleManager consoleManager;

	@Autowired
	private AgentManager agentManager;

	@Autowired
	private Config config;

	@Autowired
	private FileEntryService fileEntryService;

	@Autowired
	private ScriptHandlerFactory scriptHandlerFactory;

	@Bean
	public PerfTestService perfTestService() {
		PerfTestService service=new PerfTestService();
		service.setConfig(config);
		service.setAgentManager(agentManager);
		service.setConsoleManager(consoleManager);
		service.setFileEntryService(fileEntryService);
		service.setScriptHandlerFactory(scriptHandlerFactory);
		service.setPerfTestRepository(perfTestRepository);
		return service;

	}


}
