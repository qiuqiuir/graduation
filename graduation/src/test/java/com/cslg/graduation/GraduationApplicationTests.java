package com.cslg.graduation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class GraduationApplicationTests {

	@Test

	void contextLoads() {
		Date time = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.WEEK_OF_MONTH,-1);
		time = calendar.getTime();
		System.out.println(time);
	}

}
