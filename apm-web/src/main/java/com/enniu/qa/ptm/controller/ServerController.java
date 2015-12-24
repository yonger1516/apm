package com.enniu.qa.ptm.controller;

import com.enniu.qa.ptm.model.GNode;
import com.enniu.qa.ptm.model.Server;
import com.enniu.qa.ptm.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fuyong on 7/9/15.
 */

@Controller
@RequestMapping("/manage")
public class ServerController {
	private static final Logger logger = LoggerFactory.getLogger(ServerController.class.getSimpleName());

	@RequestMapping(value = "serverEnd", method = RequestMethod.GET)
	public String getServerNodes(ModelMap map) {
		List<Server> nodes = new ArrayList<Server>();
		nodes.add(new Server("appServer1", 1, "192.168.2.1", 1, new Date()));
		nodes.add(new Server("appServer1", 1, "192.168.2.2", 1, new Date()));
		nodes.add(new Server("appServer1", 1, "192.168.2.3", 1, new Date()));
		nodes.add(new Server("MySQL", 2, "192.168.2.4", 1, new Date()));
		nodes.add(new Server("Redis", 3, "192.168.2.5", 0, new Date()));
		map.put("nodes", nodes);

		return "/manage/serverEnd";
	}

	@RequestMapping(value = "newServer", method = RequestMethod.GET)
	public String newServer(ModelMap map) {
		Server server=new Server();
		map.put("server",server);

		return "/manage/newServer";
	}

	@RequestMapping(value = "addServer", method = RequestMethod.POST)
	public String addServer(ModelMap map) {

		return "redirect:/manage/serverEnd";
	}

	@RequestMapping(value = "deploy", method = RequestMethod.GET)
	public String deploy(ModelMap map) {

		return "/manage/deploy";
	}

	@RequestMapping(value = "/do_deploy", method = RequestMethod.POST)
	public String doDeploy(ModelMap map) {

		return "/manage/deploy";
	}
}
