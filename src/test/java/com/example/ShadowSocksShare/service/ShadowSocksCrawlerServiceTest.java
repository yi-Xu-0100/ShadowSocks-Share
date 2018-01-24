package com.example.ShadowSocksShare.service;

import com.example.ShadowSocksShare.BaseTest;
import com.example.ShadowSocksShare.domain.ShadowSocksEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
public class ShadowSocksCrawlerServiceTest extends BaseTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	@Qualifier("iShadowCrawlerServiceImpl")
	private ShadowSocksCrawlerService iShadowCrawlerServiceImpl;    // ishadow
	@Autowired
	@Qualifier("doubCrawlerServiceImpl")
	private ShadowSocksCrawlerService doubCrawlerServiceImpl;                // https://doub.io
	@Autowired
	@Qualifier("freeSS_EasyToUseCrawlerServiceImpl")
	private ShadowSocksCrawlerService freeSS_EasyToUseCrawlerServiceImpl;                // https://freess.cx/#portfolio-preview
	@Autowired
	@Qualifier("ss8ServiceImpl")
	private ShadowSocksCrawlerService ss8ServiceImpl;                // https://en.ss8.fun/
	@Autowired
	@Qualifier("freeSSRCrawlerServiceImpl")
	private ShadowSocksCrawlerService freeSSRCrawlerServiceImpl;                // https://global.ishadowx.net/
	@Autowired
	@Qualifier("free_ssServiceImpl")
	private ShadowSocksCrawlerService free_ssServiceImpl;                // https://free-ss.site/
	@Autowired
	@Qualifier("ssrBlueCrawlerServiceImpl")
	private ShadowSocksCrawlerService ssrBlueCrawlerServiceImpl;                // http://www.ssr.blue


	@Test
	public void testDoubCrawlerService() {
		ShadowSocksEntity entity = doubCrawlerServiceImpl.getShadowSocks();
		log.debug("========>{}", entity);
	}
}