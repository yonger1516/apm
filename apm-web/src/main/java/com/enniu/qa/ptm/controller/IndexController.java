package com.enniu.qa.ptm.controller;

import com.enniu.qa.ptm.configuration.constant.ControllerConstants;
import com.enniu.qa.ptm.logger.CoreLogger;
import com.enniu.qa.ptm.model.Project;
import com.enniu.qa.ptm.service.ProjectService;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by fuyong on 7/1/15.
 */
@Controller
public class IndexController extends BaseController {
	@Autowired
	ProjectService projectService;

	@RequestMapping(value={"","/"})
	public String getIndex(HttpServletRequest request, HttpSession session){
		setSession(request,session);
		return "redirect:/project/";
	}

	/**
	 * Return the login page.
	 *
	 * @param
	 * @return "login" if not logged in. Otherwise, "/"
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,HttpSession session) {
		setSession(request,session);
		return "login";
	}


}
