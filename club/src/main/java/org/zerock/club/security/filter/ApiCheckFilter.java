package org.zerock.club.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.club.security.util.JWTUtil;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter{
	
	private AntPathMatcher antPathMatcher;
	private String pattern;
	private JWTUtil jwtUtil;
	
	public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
		this.antPathMatcher = new AntPathMatcher();
		this.pattern = pattern;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
		
		log.info("REQUESTURI: " + request.getRequestURI());
		
		log.info(antPathMatcher.match(pattern, request.getRequestURI()));
		
		if(antPathMatcher.match(pattern, request.getRequestURI())) {
			
			log.info("ApiCheckFilter.......................");
			log.info("ApiCheckFilter.......................");
			log.info("ApiCheckFilter.......................");
			
			boolean checkHeader = checkAuthHeader(request); //Authorization Header 유효성 확인 
			
			if(checkHeader) {
				filterChain.doFilter(request, response);
				return;
			}else { //실패시 json 데이터 리턴. AuthenticationManager를 사용하는 방법도 있음
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				// json 리턴 및 한글깨짐 수정.
				response.setContentType("application/json;charset=utf-8");
				JSONObject json = new JSONObject();
				String message = "FAIL CHECK API TOKEN";
				json.put("code", "403");
				json.put("message", message);
				
				PrintWriter out = response.getWriter();
				out.print(json);
				return;
			}
		}
		
		filterChain.doFilter(request, response); //다음 필터의 단계로 넘어가는 역할. Security Config를 통해 스프링의 빈으로 설정
	}
	
	private boolean checkAuthHeader(HttpServletRequest request) {
		
		boolean checkResult = false;
		
		String authHeader = request.getHeader("Authorization");
		
		if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) { // JWT이용할 때 Bearer 사용
			log.info("Authorization exist: " + authHeader);
			
			try {
				String email = jwtUtil.validateAndExtract(authHeader.substring(7));
				log.info("validate result: " + email);
				checkResult = email.length() > 0;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return checkResult;
	}
}
