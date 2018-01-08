package com.example.ShadowSocksShare.service.tasks;

import com.example.ShadowSocksShare.service.ShadowSocksCrawlerService;
import com.example.ShadowSocksShare.service.ShadowSocksSerivce;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 定时抓取 ss 信息
 */
@Slf4j
@Component
public class ShadowSocksTasks {
	@Autowired
	private ShadowSocksSerivce shadowSocksSerivce;

	@Autowired
	@Qualifier("iShadowCrawlerServiceImpl")
	private ShadowSocksCrawlerService iShadowCrawlerServiceImpl;    // ishadow
	@Autowired
	@Qualifier("doubCrawlerServiceImpl")
	private ShadowSocksCrawlerService doubCrawlerServiceImpl;                // https://doub.io
	@Autowired
	@Qualifier("freeSS_EasyToUseCrawlerServiceImpl")
	private ShadowSocksCrawlerService freeSS_EasyToUseCrawlerServiceImpl;                // https://freess.cx/#portfolio-preview

	@Scheduled(cron = "0 10 */3 * * ?")
	public void iShadowCrawler() {
		shadowSocksSerivce.crawlerAndSave(iShadowCrawlerServiceImpl);
	}

	@Scheduled(cron = "0 10 */6 * * ?")
	public void doubCrawler() {
		shadowSocksSerivce.crawlerAndSave(doubCrawlerServiceImpl);
	}

	@Scheduled(cron = "0 10 */12 * * ?")
	public void freeSS_EasyToUseCrawler() {
		shadowSocksSerivce.crawlerAndSave(freeSS_EasyToUseCrawlerServiceImpl);
	}

	/**
	 * SS 有效性检查，每 1 小时
	 */
	// @Scheduled(cron = "0 */2 * * * ?")
	@Scheduled(cron = "0 0 */1 * * ?")
	public void checkValid() {
		shadowSocksSerivce.checkValid();
	}

	/**
	 * 为防止 herokuapp 休眠，每 10 分钟访问一次
	 */
	@Scheduled(cron = "0 */20 * * * ?")
	public void monitor() throws IOException {
		Jsoup.connect("https://shadowsocks-share.herokuapp.com/subscribe").get();
	}
}
