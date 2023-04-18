package com.example.SpringBatchTutorial.job.ValidatedParam;

import com.example.SpringBatchTutorial.job.ValidatedParam.Vallidator.FileParamValidator;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* desc : 파일 이름 파라미터 전달 그리고 검증
* run : --spring.batch.job.names=validatedParamJob -fileName=test.csv
* */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /* jobBuilderFactory,stepBuilderFactory 객체를 이용해, job과 step을 생성한다 */
    @Bean
    public Job validatedParamJob(Step validatedParamStep){
        return jobBuilderFactory.get("validatedParamJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FileParamValidator())
                .start(validatedParamStep)
                .build();
    }

    @Bean
    @JobScope
    public Step validatedParamStep(Tasklet validatedParamTasklet){
        return stepBuilderFactory.get("validatedParamStep")
                /*step하위 영역,읽고 쓸것 없이 단순한 배치를 만들 땐 tasklet을 만듬 */
                .tasklet(validatedParamTasklet)
                .build();
    }

    /* Tasklet 등록 */
    @Bean
    @StepScope //step하위에서 실행되므로
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("validated Param Tasklet");
                return RepeatStatus.FINISHED; //Step종료하기
            }
        };
    }
}
