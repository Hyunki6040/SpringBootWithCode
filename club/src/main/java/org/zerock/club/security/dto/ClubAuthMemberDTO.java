package org.zerock.club.security.dto;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClubAuthMemberDTO extends User{
	
	public ClubAuthMemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities){
		super(username, password, authorities);
	}
}
