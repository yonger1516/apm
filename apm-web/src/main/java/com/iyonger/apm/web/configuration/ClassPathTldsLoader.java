package com.iyonger.apm.web.configuration;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fuyong on 8/20/15.
 */
public class ClassPathTldsLoader {
	private static final String SECURITY_TLD = "/META-INF/security.tld";

	final private List<String> classPathTlds;

	public ClassPathTldsLoader(String... classPathTlds) {
		super();
		if(ArrayUtils.isEmpty(classPathTlds)){
			this.classPathTlds = Arrays.asList(SECURITY_TLD);
		}else{
			this.classPathTlds = Arrays.asList(classPathTlds);
		}
	}

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@PostConstruct
	public void loadClassPathTlds() {
		freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classPathTlds);
	}
}
