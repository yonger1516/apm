package com.iyonger.apm.web;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by fuyong on 2/19/16.
 */
@Component
public class MyListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	private int listenPort;
	@Override
	public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
		listenPort = event.getEmbeddedServletContainer().getPort();
	}

	public int getListenPort(){
		return listenPort;
	}
}
