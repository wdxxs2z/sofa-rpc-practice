package com.alipay.sofa.dtx.account.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.alipay.sofa.dtx.account.service.TransferService;
import com.alipay.sofa.dtx.mockserver.MockDtxRpcServer;

@ImportResource({"classpath*:META-INF/spring/*.xml"})
@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AccountMain {

	public static void main(String[] args) {
		
		System.out.print("start to init dtxsever(mock).");
		new MockDtxRpcServer().init();
		
        SpringApplication springApplication = new SpringApplication(AccountMain.class);
        //a non-web application
        springApplication.setWebEnvironment(true);
        ApplicationContext applicationContext = springApplication.run(args);
        System.out.println("========================== Inited. ========================== ");
        
        TransferService transferService = (TransferService) applicationContext.getBean("transferService");
        
        System.out.println("==============TCC转账开始");
        boolean ret = transferService.transferByTcc("A01", "B01", 1);
        System.out.println("==============TCC转账结束, 结果为：" + ret);
        
//        System.out.println("==============TCC转账(嵌套事务)开始");
//        boolean ret2 = transferService.transferByTccNest("A02", "B02", 1);
//        System.out.println("==============TCC转账(嵌套事务)结束, 结果为：" + ret2);
//        
//        System.out.println("==============FMT转账开始");
//        boolean ret3 = transferService.transferByAuto("A03", "B03", 1);
//        System.out.println("==============FMT转账结束, 结果为：" + ret3);
//
//        System.out.println("==============FMT转账(嵌套事务)开始");
//        boolean ret4 = transferService.transferByAutoNest("A04", "B04", 1);
//        System.out.println("==============FMT转账(嵌套事务)结束, 结果为：" + ret4);
        
        
        System.out.println("========================== DTX check finish. ========================== ");
	}

}