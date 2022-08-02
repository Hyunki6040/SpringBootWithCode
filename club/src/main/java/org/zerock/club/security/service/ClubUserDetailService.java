package org.zerock.club.security.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor // repository 주입성 추
public class ClubUserDetailService implements UserDetailsService{
	
	private final ClubMemberRepository clubMemberRepository; //주입성 추가
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{ //사용자가 존재하지 않으면 Exception 처리
		
		log.info("ClubUserDetailsService loadUserByUsername " + username);
		
		Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
		
		if(!result.isPresent()) {
			throw new UsernameNotFoundException("Check Email or Social ");
		}
		
		ClubMember clubMember = result.get();
		
		log.info("--------------------");
		log.info(clubMember);
		
		ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO( // UserDetails 타입으로 처리하기 위해서 타입 변
				clubMember.getEmail(),
				clubMember.getPassword(),
				clubMember.isFromSocial(),
				clubMember.getRoleSet().stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet()) //접두어 사용해 스프링 시큐리티에서 사용하는 SimpleGrantedAuthority로 변
		);
		
		clubAuthMember.setName(clubMember.getName());
		clubAuthMember.setFromSocial(clubMember.isFromSocial());
		
		return clubAuthMember;
	}

}
