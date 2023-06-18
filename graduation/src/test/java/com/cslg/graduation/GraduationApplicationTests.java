package com.cslg.graduation;

import com.cslg.graduation.common.KnowledgeEnum;
import com.cslg.graduation.service.ScoreService;
import com.cslg.graduation.service.SpiderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class GraduationApplicationTests {

	@Autowired
	private SpiderService spiderService;

	@Autowired
	private ScoreService scoreService;
	@Test
	void contextLoads() {
		Date date = new Date(2023-1900,1,19);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc290");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			if (entry.getKey().equals("cslg092222216")) {
				double nowScore = entry.getValue();
				System.out.println(nowScore);
				scoreService.updateScore("092222216",date,nowScore);
			}
		}

	}
	@Test
	void contextLoads1() {
		Date date = new Date(2023-1900,2,4);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc292");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			if (entry.getKey().equals("cslg092222216")) {
				double nowScore = entry.getValue();
				System.out.println(nowScore);
				scoreService.updateScore("092222216",date,nowScore);
			}
		}

	}
	@Test
	void contextLoads2() {
		Date date = new Date(2023-1900,2,25);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc295");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			if (entry.getKey().equals("cslg092222216")) {
				double nowScore = entry.getValue();
				System.out.println(nowScore);
				scoreService.updateScore("092222216",date,nowScore);
			}
		}

	}
	@Test
	void contextLoads3() {
		Date date = new Date(2023-1900,3,29);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc300");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			if (entry.getKey().equals("cslg092222216")) {
				double nowScore = entry.getValue();
				System.out.println(nowScore);
				scoreService.updateScore("092222216",date,nowScore);
			}
		}

	}
	@Test
	void contextLoads4() {
		Date date = new Date(2023-1900,4,20);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc302");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			if (entry.getKey().equals("cslg092222216")) {
				double nowScore = entry.getValue();
				System.out.println(nowScore);
				scoreService.updateScore("092222216",date,nowScore);
			}
		}

	}

}
