package com.example.SpringBatchTutorial;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling //Scheduling기능 추가
@EnableBatchProcessing //Batch가 가능하게
@SpringBootApplication
public class SpringBatchTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTutorialApplication.class, args);
	}

}
