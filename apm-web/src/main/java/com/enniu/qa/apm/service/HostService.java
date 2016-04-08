package com.enniu.qa.apm.service;

import com.enniu.qa.apm.MyListener;
import com.enniu.qa.apm.dto.HostDto;
import com.enniu.qa.apm.remotecontrol.RemoteTask;
import com.enniu.qa.apm.remotecontrol.TaskEngine;
import com.enniu.qa.apm.dao.HostDao;
import com.enniu.qa.apm.model.Host;
import com.enniu.qa.apm.util.CommonUtils;
import org.ngrinder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import static com.enniu.qa.apm.model.HostType.*;

/**
 * Created by fuyong on 2/18/16.
 */
@Service
public class HostService {
	private static final Logger logger = LoggerFactory.getLogger(HostService.class.getSimpleName());
	private static final String startGrinderAgentDaemon = "nohup /bin/sh ngrinder-agent/run_agent.sh >run.log 2>&1 &";

	@Autowired
	HostDao hostDao;

	@Autowired
	TaskEngine taskEngine;

	@Autowired
	MyListener myListener;

	public List<Host> getAllHost() {
		return hostDao.findAll();
	}

	public Host getHost(long id) {
		return hostDao.getOne(id);
	}

	public Host save(Host host) throws Exception {

		if (!isValuable(host)) {
			throw new Exception("Username or password not correct.Can't connect the target host.");
		}
		if (host.exist()) {
			Host exist = hostDao.getOne(host.getId());
			host = exist.merge(host);
		}
		return hostDao.saveAndFlush(host);
	}

	public boolean isValuable(Host host) {
		if (!host.getPassword().isEmpty()) {
			return taskEngine.testConnection(host.getHostName(), host.getUserName(), host.getPassword());
		} else {
			return false;
		}

	}


	public void installPinpointAgent() {

	}


	public void installAgent(long hostId) throws Exception {
		Host host = hostDao.findById(hostId);
		switch (host.getType()) {
			case GENERATOR:
				String packagePath = "http://" + CommonUtils.getLocalHostLANAddress().getHostAddress() + ":" + myListener.getListenPort() + "/agent/download";
				installGrinderAgent(host, packagePath);
				break;
			case SERVER:
				packagePath = "";
				installPinpointAgent();
				break;
			default:
				break;
		}

		//hostDao.saveAndFlush(host);

	}

	public void startAgent(long hostId) throws Exception {
		Host host = hostDao.findById(hostId);
		startGrinderAgent(host);

	}

	public void installGrinderAgent(Host host, String path) throws Exception {

		Vector<String> options = new Vector<String>();

		options.add("mkdir -p agent && cd agent");
		options.add("wget -q " + path);
		options.add("find . -maxdepth 1 -name ngrinder*.tar |xargs tar vxf ");
		options.add(startGrinderAgentDaemon);

		buildAndExecRemoteTask(host, "Install Agent", options);

	}

	public void startGrinderAgent(Host host) throws Exception {


		Vector<String> options = new Vector<String>();
		options.add("cd agent");
		options.add("ngrinder-agent/stop_agent.sh");
		options.add(startGrinderAgentDaemon);

		buildAndExecRemoteTask(host, "start agent", options);

	}

	public void buildAndExecRemoteTask(Host host, String taskName, Vector<String> options) throws Exception {
		RemoteTask task = new RemoteTask();
		task.setName(taskName);
		task.setUserName(host.getUserName());
		task.setPassword(host.getPassword());
		task.setTargetId(host.getHostName());
		task.setOptions(options);

		logger.info("{} on {}...", taskName, host.getHostName());
		task = taskEngine.doTask(task);
		task.getFuture().get();
		logger.info("Task done.");
	}
}
