
package com.enniu.qa.apm.configuration;

import com.enniu.qa.apm.dao.PerfTestRepository;
import com.enniu.qa.apm.handler.ScriptHandlerFactory;
import com.enniu.qa.apm.model.AgentManager;
import com.enniu.qa.apm.service.ConsoleManager;
import com.enniu.qa.apm.service.FileEntryService;
import com.enniu.qa.apm.service.PerfTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
