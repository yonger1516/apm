package com.enniu.qa.apm.controller;

import com.enniu.qa.apm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

	@RequestMapping(value = "/login/header")
	public String login2(@RequestHeader("host")String host,@RequestHeader("X-Tracking-ID")String trackId,HttpServletRequest request,HttpSession session) {
		setSession(request,session);
		return "login";
	}


}
