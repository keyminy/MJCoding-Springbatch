package com.example.SpringBatchTutorial.job.HelloWorld;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.SpringBatchTutorial.SpringBatchTestConfig;

@RunWith(SpringRunner.class)
@ActiveProfiles("test") //h2 DB로 테스트
@SpringBatchTest
//SpringBatchTestConfig.class : 환경설정 클래스, HelloWorldJobConfig.class : 테스트할 target
@SpringBootTest(classes = {SpringBatchTestConfig.class,HelloWorldJobConfig.class})
public class HelloWorldJobConfigTest {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Test
	public void success() throws Exception {
		/*when*/
		//HeloWorldJobConfig실행
		//JobExecution으로 결과 확인하자
		JobExecution execution = jobLauncherTestUtils.launchJob();
		
		/*then*/
		//예상결과 : 성공
		Assertions.assertEquals(ExitStatus.COMPLETED,execution.getExitStatus());
	}
}
