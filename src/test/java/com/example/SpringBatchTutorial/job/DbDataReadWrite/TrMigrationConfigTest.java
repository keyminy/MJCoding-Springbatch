package com.example.SpringBatchTutorial.job.DbDataReadWrite;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
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
import com.example.SpringBatchTutorial.core.domain.accounts.AccountsRepository;
import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import com.example.SpringBatchTutorial.core.domain.orders.OrdersRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test") //h2 DB로 테스트
@SpringBatchTest
//SpringBatchTestConfig.class : 환경설정 클래스, TrMigrationConfigTest.class : 테스트할 target
@SpringBootTest(classes = {SpringBatchTestConfig.class,TrMigrationConfig.class})
public class TrMigrationConfigTest {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	//주문테이블과 정산 테이블 활용 Repository들 주입받아
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;
    
    //테스트 코드 메서드의 순서가 변경되면 실패가 나므로..
    //각각의 작업이 끝난담에 orderRepository를 지우는 메서드 하자
    @AfterEach
    public void cleanUpEach() {
    	ordersRepository.deleteAll();
    	accountsRepository.deleteAll();
    }
    
    
    @Test
    public void success_noData() throws Exception {
    	//when
		JobExecution execution = jobLauncherTestUtils.launchJob();
		
		//then
		Assertions.assertEquals(ExitStatus.COMPLETED,execution.getExitStatus());
		//데이터 없으니까 정산 테이블에는 0개 맞는지??
		Assertions.assertEquals(0, accountsRepository.count());
		
    }
    
    @Test()
    public void success_existData() throws Exception{
    	//given : 데이터제공
    	Orders orders1 = new Orders(null,"kakao gift",15000,new Date());
    	Orders orders2 = new Orders(null,"naver gift",15000,new Date());
    	ordersRepository.save(orders1);
    	ordersRepository.save(orders2);
    	
    	//when : Job실행
    	JobExecution execution = jobLauncherTestUtils.launchJob();
    	
    	//then
    	Assertions.assertEquals(2, accountsRepository.count());
    }
	
}
