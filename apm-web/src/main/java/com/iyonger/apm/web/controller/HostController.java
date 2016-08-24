package com.iyonger.apm.web.controller;

import com.iyonger.apm.web.dto.HostDto;
import com.iyonger.apm.web.model.Host;
import com.iyonger.apm.web.service.AgentManagerService;
import com.iyonger.apm.web.service.HostService;
import net.grinder.message.console.AgentControllerState;
import org.ngrinder.model.GrinderAgentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fuyong on 2/18/16.
 */

@Controller
@RequestMapping("/host")
public class HostController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(HostController.class.getSimpleName());

	@Autowired
	HostService hostService;

	@Autowired
	AgentManagerService agentManagerService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getList(ModelMap map) {
		List<HostDto> hostDtos = new ArrayList<HostDto>();
		List<Host> hosts = hostService.getAllHost();

		for (Host h : hosts) {
			HostDto dto = new HostDto();

			dto.setIp(h.getHostName());
			dto.setId(h.getId());
			dto.setType(h.getType());

			GrinderAgentInfo agent = agentManagerService.getAgentByIp(h.getHostName());
			if (null == agent) {
				dto.setStatus(AgentControllerState.UNKNOWN);
			} else {
				dto.setStatus(agent.getState());
			}

			hostDtos.add(dto);
		}
		map.put("hosts", hostDtos);
		return "/host/list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String getNewPage(ModelMap map) {
		Host host = new Host();
		map.put("host", host);
		return "/host/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.PUT)
	@ResponseBody
	public Object addHost(HostDto dto) throws Exception {
		ModelMap map = new ModelMap();
		try {
			hostService.save(dto2Host(dto));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
		}

		return map;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String updateHost(Host host) {
		return null;
	}

	@RequestMapping(value = "/{id}/installGrinderAgent")
	@ResponseBody
	public Object installGrinderAgent(@PathVariable("id") long id) throws Exception {

		Map object = new HashMap();
		object.put("success", true);
		return object;
	}

	@RequestMapping(value = "/{id}/installAgent")
	@ResponseBody
	public Object installAgents(@PathVariable("id") long hostId) {
		ModelMap map = new ModelMap();
		try {
			hostService.installAgent(hostId);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}

		return map;
	}

	@RequestMapping(value = "/{id}/installAgent")
	@ResponseBody
	public Object startAgents(@PathVariable("id") long hostId) {
		ModelMap map = new ModelMap();
		try {
			hostService.startAgent(hostId);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}

		return map;
	}

	public Host dto2Host(HostDto dto) {
		Host host = new Host();

		host.setId(dto.getId());
		host.setHostName(dto.getIp());
		host.setType(dto.getType());
		host.setUserName(dto.getUserName());
		host.setPassword(dto.getPassword());
		host.setSslKey("");

		return host;
	}

}
