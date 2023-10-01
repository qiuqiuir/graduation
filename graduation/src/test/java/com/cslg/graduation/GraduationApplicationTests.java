package com.cslg.graduation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.common.KnowledgeEnum;
import com.cslg.graduation.entity.Acnumber;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.*;
import com.cslg.graduation.util.GetUrlJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@SpringBootTest
class GraduationApplicationTests {

	@Autowired
	private SpiderService spiderService;

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private AcnumberService acnumberService;

	@Autowired
	private UserService userService;

	@Autowired
	private OjService ojService;

	@Autowired
	private WeekService weekService;
	@Test
	void contextLoads() throws InterruptedException {
//		Calendar now = Calendar.getInstance();
//		String year = String.valueOf(now.get(Calendar.YEAR));
//		String mouth = String.valueOf(now.get(Calendar.MONTH) + 1);
//		if (mouth.length() == 1) mouth = "0" + mouth;
//		String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
//		if (day.length() == 1) day = "0" + day;
		String time = "2023-07-09";
		Date date = new Date(2023-1900,6,9);
		System.out.println("当前时间:" + date);
		System.out.println("开始计算" + time + "的过题数");
		String url = "http://47.94.81.95:8081/rank/list?page=1&size=100&keyword=&date=" + time;
		JSONObject data = null;
		try {
			data = GetUrlJson.getHttpJson(url);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		data = data.getJSONObject("msg");
		data = data.getJSONObject("data");
		JSONArray records = data.getJSONArray("records");
		List<User> userList = userService.getAllUsers();
		for (int i = 0; i < records.size(); i++) {
			String username = records.getJSONObject(i).getString("username");
			if (userService.findUserByUsername(username) == null) continue;
			Acnumber acnumber = new Acnumber()
					.setUsername(username)
					.setTime(date);
			int atcoder = records.getJSONObject(i).getInteger("atcoder");
			acnumber = acnumber.setPlatform("atcoder").setCount(atcoder);
			acnumberService.addAcnumber(acnumber);

			int nowcoder = records.getJSONObject(i).getInteger("niuke");
			acnumber = acnumber.setPlatform("nowcoder").setCount(nowcoder);
			acnumberService.addAcnumber(acnumber);

			int cf = records.getJSONObject(i).getInteger("codeforces");
			acnumber = acnumber.setPlatform("codeforces").setCount(cf);
			acnumberService.addAcnumber(acnumber);

			int vjudge = records.getJSONObject(i).getInteger("vjudge");
			acnumber = acnumber.setPlatform("vjudge").setCount(vjudge);
			acnumberService.addAcnumber(acnumber);

			int luogu = records.getJSONObject(i).getInteger("luogu");
			acnumber = acnumber.setPlatform("luogu").setCount(luogu);
			acnumberService.addAcnumber(acnumber);

		}
		date = new Date();
		System.out.println("当前时间:" + date);
		System.out.println(time + "过题数计算已完成，已保存至数据库");

	}
	@Test
	void contextLoads1() {
//		Date date = new Date(2023-1900,10,4);
		Map<String,Double> rankData = spiderService.getAtcoderScore("abc321");
		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
			System.out.print(userService.findUserByUsername(entry.getKey()).getUsername()+" ");
			System.out.println(entry.getKey()+" "+entry.getValue());
		}

	}
	@Test
	void contextLoads2() {
//		Date date = new Date(2023-1900,2,25);
//		Map<String,Double> rankData = spiderService.getAtcoderScore("abc295");
//		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
//			if (entry.getKey().equals("cslg092222216")) {
//				double nowScore = entry.getValue();
//				System.out.println(nowScore);
//				scoreService.updateScore("092222216",date,nowScore);
//			}
//		}

	}
	@Test
	void contextLoads3() {
//		Date date = new Date(2023-1900,3,29);
//		Map<String,Double> rankData = spiderService.getAtcoderScore("abc300");
//		for (Map.Entry<String, Double> entry : rankData.entrySet()) {
//			if (entry.getKey().equals("cslg092222216")) {
//				double nowScore = entry.getValue();
//				System.out.println(nowScore);
//				scoreService.updateScore("092222216",date,nowScore);
//			}
//		}

	}
	@Test
	void contextLoads4() throws InterruptedException {
		String id="Drmeng";
		String username = "093121109";
		String platform = "codeforces";
		Map<String, Integer> rating = spiderService.getCfRating(id);
		if(rating.get("current")!=null) {
			ojService.updateNowRating(username, platform, id, rating.get("current"));
		}
		if(rating.get("history")!=null) {
			ojService.updateHistoryRating(username, platform, id, rating.get("history"));
		}
	}

	@Test
	void test1(){
		List<Week> weekList = weekService.getLegalAllWeek();
		Collections.sort(weekList, new Comparator<Week>() {
			@Override
			public int compare(Week o1, Week o2) {
				return o2.getTime().compareTo(o1.getTime());
			}
		});
		List<Score> scoreList = scoreService.getScoresByTime(weekList.get(0).getTime());
		for (Score score : scoreList){
			System.out.println(score);
		}
	}

}
