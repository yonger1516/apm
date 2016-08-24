
package com.iyonger.apm.web.configuration;

import com.iyonger.apm.web.dao.PerfTestRepository;
import com.iyonger.apm.web.handler.ScriptHandlerFactory;
import com.iyonger.apm.web.model.AgentManager;
import com.iyonger.apm.web.service.ConsoleManager;
import com.iyonger.apm.web.service.FileEntryService;
import com.iyonger.apm.web.service.PerfTestService;
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
