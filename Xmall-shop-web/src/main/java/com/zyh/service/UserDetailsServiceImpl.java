package com.zyh.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * ��֤��
 * @author Administrator
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();  
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));          
        return new User(username,"123456", grantedAuths);
	}
}
