package com.enniu.qa.ptm.controller;

import com.enniu.qa.ptm.model.GNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fuyong on 7/9/15.
 */

@Controller
@RequestMapping("/manage")
public class GeneratorController {
	private static final Logger logger = LoggerFactory.getLogger(GeneratorController.class.getSimpleName());

	@RequestMapping(value = "generatorEnd", method = RequestMethod.GET)
	public String getGNodes(ModelMap map) {
		List<GNode> nodes = new ArrayList<GNode>();
		nodes.add(new GNode("controller", 1, "192.168.2.1", 1, new Date()));
		nodes.add(new GNode("node1", 2, "192.168.2.2", 1, new Date()));
		nodes.add(new GNode("node2", 2, "192.168.2.3", 1, new Date()));
		nodes.add(new GNode("node3", 2, "192.168.2.4", 1, new Date()));
		nodes.add(new GNode("node4", 2, "192.168.2.5", 0, new Date()));
		map.put("nodes", nodes);

		return "/manage/generatorEnd";
	}

	@RequestMapping(value = "newGNode", method = RequestMethod.GET)
	public String newGNode(ModelMap map) {
		GNode node=new GNode();
		map.put("gNode",node);
		return "/test/newGNode";
	}

	@RequestMapping(value = "addGNode", method = RequestMethod.POST)
	public String addGNode(ModelMap map) {
		GNode node=new GNode();
		map.put("gNode",node);

		return "redirect:/test/generatorEnd";
	}
}
