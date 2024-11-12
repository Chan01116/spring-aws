package com.myaws.myapp.aop;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component  //빈으로 등록하기 위한 부모클래스
@Aspect
//@Slf4j  롬복 라이브러리 추가할때 사용
public class SampleAdvice {
	
	private static final Logger  logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	
	@Before("execution(* com.myaws.myapp.service.BoardService*.*(..))") //모든 파일
	public void startLog() {
		
		//logger.info("-----------------------");
		//logger.info("aop 로그 테스트중입니다");
		//logger.info("-----------------------");
		//System.out.println("테스트");
		
		
		
	}
	
	

}
