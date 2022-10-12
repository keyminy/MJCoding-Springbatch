package com.example.SpringBatchTutorial.job.HelloWorld;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 * run : --spring.batch.job.names=helloWorldJob
 * */
@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /* jobBuilderFactory,stepBuilderFactory 객체를 이용해, job과 step을 생성한다 */
    @Bean
    public Job helloWorldJob(){
        return jobBuilderFactory.get("helloWorldJob")
                .incrementer(new RunIdIncrementer())
                /*job안에는 Step이 존재한다, Step을 만들어줌 */
                .start(helloWorldStep())
                .build();
    }

    @Bean
    @JobScope
    public Step helloWorldStep(){
        return stepBuilderFactory.get("helloWorldStep")
                /*step하위 영역,읽고 쓸것 없이 단순한 배치를 만들 땐 tasklet을 만듬 */
                .tasklet(helloWorldTasklet())
                .build();
    }

    /* Tasklet 등록 */
    @Bean
    @StepScope //step하위에서 실행되므로
    public Tasklet helloWorldTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Hello World Spring Batch");
                return RepeatStatus.FINISHED; //Step종료하기
            }
        };
    }
}
