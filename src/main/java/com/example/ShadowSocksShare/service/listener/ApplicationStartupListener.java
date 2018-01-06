package com.example.ShadowSocksShare.service.listener;

import com.example.ShadowSocksShare.service.tasks.ShadowSocksTasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 系统启动 监听事件
 */
@Slf4j
@Component
public class ApplicationStartupListener {
	@Autowired
	private ShadowSocksTasks shadowSocksTasks;


	/**
	 * 系统启动 监听事件
	 */
	@EventListener
	public void handleOrderStateChange(ContextRefreshedEvent contextRefreshedEvent) {
		log.debug(contextRefreshedEvent.toString());
		shadowSocksTasks.runAll();
	}
}
