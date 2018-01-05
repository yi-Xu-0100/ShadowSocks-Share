package com.example.ShadowSocksShare.service.impl;

import com.example.ShadowSocksShare.domain.ShadowSocksDetailsEntity;
import com.example.ShadowSocksShare.service.ShadowSocksCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FreeSS-Easy to Use
 * https://freess.cx/#portfolio-preview
 */
@Slf4j
@Service
public class FreeSS_EasyToUseCrawlerServiceImpl extends ShadowSocksCrawlerService {
	// 网站刷新时间（抓取SS信息间隔时间）
	public static final int REFRESH_TIME = 5 * 60 * 60 * 1000;
	// 目标网站 URL
	private static final String TARGET_URL = "https://freess.cx/";

	/**
	 * 网页内容解析 ss 信息
	 *
	 * @param document
	 */
	@Override
	protected Set<ShadowSocksDetailsEntity> parse(Document document) {
		Elements ssList = document.select("div.4u");

		Set<ShadowSocksDetailsEntity> set = new HashSet(ssList.size());

		for (int i = 0; i < ssList.size(); i++) {
			try {
				Element element = ssList.get(i);
				// 取 a 信息，为 ss 信息
				String ssURL = TARGET_URL + element.select("a").first().attributes().get("href");

				ShadowSocksDetailsEntity ss = parseURL(ssURL);
				ss.setValid(false);
				ss.setValidTime(new Date());
				ss.setRemarks(TARGET_URL);
				ss.setGroup("ShadowSocks-Share");

				// 测试网络
				if (isReachable(ss))
					ss.setValid(true);

				// 无论是否可用都入库
				set.add(ss);

				log.debug("*************** 第 {} 条 ***************{}{}", i + 1, System.lineSeparator(), ss);
				// log.debug("{}", ss.getLink());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return set;
	}

	/**
	 * 目标网站 URL
	 */
	@Override
	protected String getTargetURL() {
		return TARGET_URL;
	}
}
