package com.welfare.fundraising.listener;

import javax.servlet.ServletContextEvent;

@SuppressWarnings("deprecation")
public class FundraisingListener extends org.springframework.web.util.Log4jConfigListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}

}
