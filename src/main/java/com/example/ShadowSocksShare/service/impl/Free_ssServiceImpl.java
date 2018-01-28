package com.example.ShadowSocksShare.service.impl;

import com.example.ShadowSocksShare.domain.ShadowSocksDetailsEntity;
import com.example.ShadowSocksShare.domain.ShadowSocksEntity;
import com.example.ShadowSocksShare.service.ShadowSocksCrawlerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * https://free-ss.site/
 */
@Slf4j
@Service
public class Free_ssServiceImpl extends ShadowSocksCrawlerService {
	// 目标网站 URL
	private static final String TARGET_URL = "https://free-ss.site/ss.json?_={0}";

	public ShadowSocksEntity getShadowSocks() {
		// 1. 爬取账号

		String ssListJson = null;
		try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.getOptions().setJavaScriptEnabled(true);                // 启动JS
			webClient.setJavaScriptTimeout(10 * 1000);                            // 设置JS执行的超时时间
			webClient.getOptions().setUseInsecureSSL(true);                    // 忽略ssl认证
			webClient.getOptions().setCssEnabled(false);                    // 禁用Css，可避免自动二次请求CSS进行渲染
			webClient.getOptions().setThrowExceptionOnScriptError(false);   //运行错误时，不抛出异常
			webClient.getOptions().setTimeout(SOCKET_TIME_OUT);                // 连接超时时间。如果为0，则无限期等待
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置Ajax异步

			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 忽略错误的 Http code


			// 模拟浏览器打开一个目标网址
			HtmlPage htmlPage = webClient.getPage(getTargetURL());
			webClient.waitForBackgroundJavaScript(10 * 1000); // 等待 JS 执行时间


			// 提交：1. 添加一个 submit；2. 添加到 form；3. 点击按钮
			DomElement button = htmlPage.createElement("button");
			button.setAttribute("type", "submit");

			final DomElement form = htmlPage.getElementById("challenge-form");
			form.appendChild(button);

			Page page = button.click();

			ssListJson = page.getWebResponse().getContentAsString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		// log.debug("========= > ssListJson:{}", ssListJson);

		// 2. 解析 json 生成 ShadowSocksDetailsEntity
		Set<ShadowSocksDetailsEntity> set = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, List<List<String>>> map = mapper.readValue(ssListJson, new TypeReference<Map<String, List<List<String>>>>() {});

			if (map.containsKey("data")) {
				List<List<String>> strList = map.get("data");
				set = new HashSet(strList.size());
				for (int i = 0; i < strList.size(); i++) {
					// 100, "144.217.163.62", "4436", "^CT&zd7*Ra", "aes-256-cfb", "09:32:05", "CA"
					ShadowSocksDetailsEntity ss = new ShadowSocksDetailsEntity(strList.get(i).get(1), Integer.parseInt(strList.get(i).get(2)), strList.get(i).get(3), strList.get(i).get(4), SS_PROTOCOL, SS_OBFS);
					ss.setValid(false);
					ss.setValidTime(new Date());
					ss.setTitle("free-ss.site");
					ss.setRemarks("https://free-ss.site/");
					ss.setGroup("ShadowSocks-Share");

					// 测试网络
					if (isReachable(ss))
						ss.setValid(true);

					// 无论是否可用都入库
					set.add(ss);

					log.debug("*************** 第 {} 条 ***************{}{}", i + 1, System.lineSeparator(), ss);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		// 3. 生成 ShadowSocksEntity
		ShadowSocksEntity entity = new ShadowSocksEntity(getTargetURL(), "free-ss.site", true, new Date());
		entity.setShadowSocksSet(set);
		return entity;
		// return new ShadowSocksEntity(getTargetURL(), "", false, new Date());
	}

	/**
	 * 网页内容解析 ss 信息
	 */
	@Override
	protected Set<ShadowSocksDetailsEntity> parse(Document document) {
		return null;
	}

	/**
	 * 目标网站 URL
	 */
	@Override
	protected String getTargetURL() {
		return MessageFormat.format(TARGET_URL, String.valueOf(System.currentTimeMillis()));
	}
}
