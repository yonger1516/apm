package com.iyonger.apm.web.controller;

import com.iyonger.apm.web.configuration.Config;
import com.iyonger.apm.web.model.Role;
import com.iyonger.apm.web.service.UserService;
import org.ngrinder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuyong on 7/23/15.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	@Autowired
	private UserService userService;

	@Autowired
	protected Config config;

	private ServletContext servletContext;

	@PreAuthorize("hasAnyRole('A')")
	@RequestMapping({"","/"})
	public String getAll(ModelMap model,@RequestParam(required = false) Role role,@RequestParam(required = false)String keyWords){
		return null;
	}

	@RequestMapping("new")
	@PreAuthorize("hasAnyRole('A') or #user.userId==#userId")
	public String newUser(User user,ModelMap map){
		User one=new User();
		map.put("user",user);

		return "user/new";

	}

	@PreAuthorize("hasAnyRole('A') or #user.id == #updatedUser.id")
	@RequestMapping(value = "save",method = RequestMethod.POST)
	@ResponseBody
	public Object save(User user){
		User newUser=userService.save(user);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("success",true);
		map.put("userId",newUser.getUserId());

		return map;
	}

	@RequestMapping("/profile")
	public String getUser(String userId,ModelMap map){
		User user=userService.getUserById(userId);
		map.put("user",user);

		return "user/profile";
	}





}
