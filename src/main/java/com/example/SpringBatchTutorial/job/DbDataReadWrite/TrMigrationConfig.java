package com.example.SpringBatchTutorial.job.DbDataReadWrite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import com.example.SpringBatchTutorial.core.domain.accounts.Accounts;
import com.example.SpringBatchTutorial.core.domain.accounts.AccountsRepository;
import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import com.example.SpringBatchTutorial.core.domain.orders.OrdersRepository;

import lombok.RequiredArgsConstructor;

/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관
 * run: --spring.batch.job.names=trMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {
	
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader trOrdersReader
    		,ItemProcessor trOrderProcessor
    		,ItemWriter trOrdersWriter) {
        return stepBuilderFactory.get("trMigrationStep")
        		//Batch에서, chunk단위로 트랜잭션 제공한다.
        		.<Orders,Accounts>chunk(5) //Orders(Input),Accounts(Output),chunkSize : 5
        		.reader(trOrdersReader) // reader(ItemReader<? extends I> reader)
        		.processor(trOrderProcessor)
        		.writer(trOrdersWriter)
                .build();
    }
    
//    @StepScope
//    @Bean
//    public RepositoryItemWriter<Accounts> trOrdersWriter(){
//    	return new RepositoryItemWriterBuilder<Accounts>()
//    			.repository(accountsRepository)
//    			.methodName("save")
//    			.build();
//    }
//
    
    /*RepositoryItemWriter를 사용하지 않고도, ItemWriter로도 구현 가능 */
    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrdersWriter(){
    	return new ItemWriter<Accounts>() {
			@Override
			public void write(List<? extends Accounts> items) throws Exception {
				items.forEach(item -> accountsRepository.save(item));
			}
		};
    }
    
    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor(){
    	return new ItemProcessor<Orders, Accounts>() {
			@Override
			public Accounts process(Orders item) throws Exception {
				return new Accounts(item);
			}
    		
		};
    }
    
    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader(){
    	return new RepositoryItemReaderBuilder<Orders>()
    			.name("trOrdersReader")
    			.repository(ordersRepository)
    			.methodName("findAll")
    			.pageSize(5) //chunk사이즈와 읽어올 pageSize는 같게
    			.arguments(Arrays.asList())
    			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
    			.build();
    }
}
