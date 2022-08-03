package org.zerock.club.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.club.security.util.JWTUtil;

public class JWTTests {
	
	private JWTUtil jwtUtil;
	
	@BeforeEach
	public void testBefore() {
		
		System.out.println("testBefore............");
		jwtUtil = new JWTUtil(); 	// 스프링을 이용하는 테스트가 아니므로 내부에서 객체를 만들어 테스트
	}
	
	@Test
	public void testEncoe() throws Exception{
		String email = "user95@zerock.org";
		
		String str = jwtUtil.generateToken(email);
		
		Thread.sleep(5000);
		
		String resultEmail = jwtUtil.validateAndExtract(str);
				
		System.out.println(resultEmail);
	}
}
