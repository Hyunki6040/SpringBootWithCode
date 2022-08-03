package org.zerock.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.util.JWTUtil;

import ch.qos.logback.classic.pattern.Util;
import lombok.extern.log4j.Log4j2;

//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.authorizeRequests()
		.antMatchers("/sample/all").permitAll() //로그인(인증) 없이 모든 사용자 접근가능, **/* 처럼 앤트 스타일로도 표기가능
		.antMatchers("/sample/member").hasRole("USER");
		
		http.formLogin();
		http.csrf().disable(); //REST 방식으로 이용할 수 있는 보안 설정을 다루기위해 CSRF 토큰을 발행X
		http.logout(); //로그아웃 처리 가능
		
		http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class); //패스워드를 기반으로 동작하는 Username Filter 이전에 apiCheckFilter를 먼저 동작하도록
		http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public ApiLoginFilter apiLoginFilter() throws Exception{
		
		ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil()); //로그인 경로 지정 
		apiLoginFilter.setAuthenticationManager(authenticationManager());
		
		apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
		
		return apiLoginFilter;
	}

	@Bean
	public JWTUtil jwtUtil() { //JWTUtil을 생성자에서 사용할 수 있도록
		return new JWTUtil();
	}
	
	@Bean
	public ApiCheckFilter apiCheckFilter() {
		return new ApiCheckFilter("/notes/**/*", jwtUtil()); //해당 경우에만 checkFilter가 작동하도록
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//		
//		//사용자 계정은 user1
//		auth.inMemoryAuthentication().withUser("user1")
//			//1111 패스워드 인코딩 결과
//			.password("$2a$10$.d2jOagHUav51F6N7OdoDOzt2MnALItuIF1xtUr6G6Y9vnCr.EREq")
//			.roles("USER");
//	}
}
