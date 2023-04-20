package com.example.SpringBatchTutorial.core.scheduler;

import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component //Bean으로 등록
public class SampleScheduler {
	
	@Autowired
	private Job helloWorldJob;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	//해당 메소드 스케쥴링 할 수 있게 cron등록,1분마다 실행
	//초,분,시간,일,월,주
	@Scheduled(cron = "0 */1 * * * *")
	public void helloworldJobRun() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters jobParameters = new JobParameters(
				Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis()))
		);
		//job과 JobParameters를 넣음 1분마다 실행된다!!
		jobLauncher.run(helloWorldJob,jobParameters);
	}
}
