package org.zerock.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;

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
