package com.example.SpringBatchTutorial.job.JobListener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobLoggerListener implements JobExecutionListener{
	
	//Job이름
	private static String BEFORE_MESSAGE = "{} Job is Running";
	//Job이름, Job상태
	private static String AFTER_MESSAGE = "{} Job is Done! (Status : {})";
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(BEFORE_MESSAGE,jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(AFTER_MESSAGE,
				jobExecution.getJobInstance().getJobName(),
				jobExecution.getStatus()
				);
		if(jobExecution.getStatus() == BatchStatus.FAILED) {
			//email이나 메신저 보내기..
			log.info("Job is Failed,BatchStatus fail!!");
		}
	}
	
}
