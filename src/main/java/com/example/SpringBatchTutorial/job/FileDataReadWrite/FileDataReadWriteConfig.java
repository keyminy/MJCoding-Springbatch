package com.example.SpringBatchTutorial.job.FileDataReadWrite;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.Player;
import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.PlayerYears;

import lombok.RequiredArgsConstructor;

/**
 * desc: csv 파일 읽고 쓰기
 * run: --spring.batch.job.names=fileReadWriteJob
 */
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep) {
        return jobBuilderFactory.get("fileReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }
    
    @JobScope
    @Bean
    public Step fileReadWriteStep(ItemReader playerItemReader
    		,ItemProcessor playerItemProcessor
    		,ItemWriter playerItemWriter) {
        return stepBuilderFactory.get("fileReadWriteStep")
        		.<Player,PlayerYears>chunk(5)
        		.reader(playerItemReader)
//        		.writer(new ItemWriter() {
//					@Override
//					public void write(List items) throws Exception {
//						items.forEach(System.out::println);
//					}
//				})
        		.processor(playerItemProcessor)
        		.writer(playerItemWriter)
                .build();
    }
    
    /*ItemProcessor : Player -> PlayerYears로 변경 */
    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerItemProcessor(){
    	return new ItemProcessor<Player, PlayerYears>() {
			@Override
			public PlayerYears process(Player item) throws Exception {
				return new PlayerYears(item);
			}
		};
    }
    
    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerItemReader(){
    	return new FlatFileItemReaderBuilder<Player>()
    			.name("playerItemReader")
    			.resource(new FileSystemResource("Players.csv"))
    			.lineTokenizer(new DelimitedLineTokenizer()) // lineTokenizer : data를 구분하는 기준,기본 ","로 구분
    		    //FieldSetMapper : FieldSet -> Player객체로 변경
    			.fieldSetMapper(new PlayerFieldSetMapper())//읽어온 데이터를 객체로 변경한다,FieldSetMapper를 이용함
    			.linesToSkip(1) //첫번째 제목 행은 skip
    			.build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter(){
    	//FlatFileItemWriter에서는, 어떤 Field를 사용할지 명시하기 위해 BeanWrapperFieldExtractor필요
    	BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
    	fieldExtractor.setNames(new String[] {"ID","lastName","position","yearsExperience"});
    	fieldExtractor.afterPropertiesSet();
    	DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
    	lineAggregator.setDelimiter(",");
    	lineAggregator.setFieldExtractor(fieldExtractor); //fieldExtractor 설정한 필드추출
    	
    	//FileResource생성
    	FileSystemResource outputResource = new FileSystemResource("players_output.txt");
    	return new FlatFileItemWriterBuilder<PlayerYears>()
    			.name("playerItemWriter")
    			.resource(outputResource)
    			.lineAggregator(lineAggregator) //구분자 ","
    			.build();
    }
    
}